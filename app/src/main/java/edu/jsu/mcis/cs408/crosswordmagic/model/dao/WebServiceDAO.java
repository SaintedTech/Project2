package edu.jsu.mcis.cs408.crosswordmagic.model.dao;

import android.provider.UserDictionary;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.model.Word;

public class WebServiceDAO {
    private final String TAG = "WebServiceDAO";
    private final DAOFactory daoFactory;
    WebServiceDAO(DAOFactory daoFactory) { this.daoFactory = daoFactory; }
    private static final String HTTP_METHOD = "GET";
    private static final String ROOT_URL = "http://ec2-3-142-171-53.us-east-2.compute.amazonaws.com:8080/CrosswordMagicServer/puzzle";
    private String requestUrl;
    private ExecutorService pool;


    public ArrayList<PuzzleListItem> list() {

        requestUrl = ROOT_URL;
        ArrayList<PuzzleListItem> result = null;
        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();
            pool.shutdown();
            JSONArray json = new JSONArray(response);
            // ...
            result = new ArrayList<>();
            for(int i = 0; i < json.length(); i++){
                JSONObject object = json.getJSONObject(i);
                PuzzleListItem temp =  new PuzzleListItem(object.getInt("id"), object.get("name").toString());
                result.add(temp);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return result;
    }
    public Integer addPuzzleByIDToDatabase(int id) {

        if(id==0)
            return 0;
        StringBuilder builder = new StringBuilder().append(ROOT_URL).append("?id=").append(String.valueOf(id));
        requestUrl = builder.toString();
        Puzzle result = null;
        HashMap<String, String> puzzleHM = new HashMap<>();
        int puzzleID = 0;
        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();
            pool.shutdown();
            JSONObject json = new JSONObject(response);
            puzzleHM.put("name", json.getString("name"));
            puzzleHM.put("width", String.valueOf(json.getInt("width")));
            puzzleHM.put("height", String.valueOf(json.getInt("height")));
            puzzleHM.put("description", json.getString("description"));
            result = new Puzzle(puzzleHM);
            PuzzleDAO puzzleDAO = daoFactory.getPuzzleDAO();
            WordDAO wordDAO = daoFactory.getWordDAO();
            puzzleID = puzzleDAO.create(result);

            // ..
            JSONArray jsonWordsArray = json.getJSONArray("puzzle");
            ArrayList<Word> wordsArrayList = new ArrayList<>();
            for(int i = 0; i < jsonWordsArray.length(); i++){

                HashMap<String, String> wordsHM =  new HashMap<>();
                JSONObject word = jsonWordsArray.getJSONObject(i);
                wordsHM.put("puzzleid", String.valueOf(puzzleID));
                wordsHM.put("clue", word.getString("clue"));
                wordsHM.put("column", String.valueOf(word.getInt("column")));
                wordsHM.put("box", String.valueOf(word.getInt("box")));
                wordsHM.put("row", String.valueOf(word.getInt("row")));
                wordsHM.put("direction", String.valueOf(word.getInt("direction")));
                wordsHM.put("word", word.getString("word"));

                Word realW = new Word(wordsHM);
                int wordId = wordDAO.create(realW);
                realW.setWordID(Integer.valueOf(wordId));

                wordsArrayList.add(realW);

            }
            result.addWordsToPuzzle(wordsArrayList);
        }
        catch (Exception e) { e.printStackTrace(); }
        return Integer.valueOf(puzzleID);
    }

    public class CallableHTTPRequest implements Callable<String> {
        @Override
        public String call() {

            StringBuilder r = new StringBuilder();
            String line;

                if (Thread.currentThread().isInterrupted()) {
                    Log.e(TAG, "Thread was already interrupted before HTTP started");

                    // Optional: print stack trace of who might have done it
                    for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                        Log.e(TAG, "STACK TRACE: " + ste.toString());
                    }
                }

                JSONObject results = null;
                HttpURLConnection conn = null;


                /* Log Request Data */

                try {


                    try {

                        if (Thread.interrupted()){

                            throw new InterruptedException();
                        }

                        // HTTP logic
                    } catch (InterruptedException e) {
                        Log.w("HTTPRequestTask", "Task was interrupted.");
                        return null;
                    }
                    /* Create Request */
                    URL url = new URL(requestUrl);
                    Log.w(TAG, "URL created");

                    conn = (HttpURLConnection)url.openConnection();

                    Log.w(TAG, "Opened connection");

                    conn.setReadTimeout(10000); /* ten seconds */
                    conn.setConnectTimeout(15000); /* fifteen seconds */

                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    Log.w(TAG, "Set method and timeouts");

                    /* Send Request */
                    Log.w(TAG, "Connecting...");

                    conn.connect();
                    Log.w(TAG, "Connected. Response Code: " + conn.getResponseCode());

                    // Check if task has been interrupted

                    if (Thread.interrupted())
                        throw new InterruptedException();

                    /* Get Reader for Results */

                    int code = conn.getResponseCode();

                    if (code == HttpsURLConnection.HTTP_OK || code == HttpsURLConnection.HTTP_CREATED) {

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        while ((line = reader.readLine()) != null) {
                            r.append(line);
                        }

                    }

                    //Check if task has been interrupted

                    if (Thread.interrupted())
                        throw new InterruptedException();

                    /* Parse Response as JSON */

                    results = new JSONObject(r.toString());

                }
                catch (Exception e) {
                    Log.e(TAG, " Exception: ", e);
                }
                finally {
                    if (conn != null) { conn.disconnect(); }
                }

                /* Finished; Log and Return Results */

                Log.d(TAG, " JSON: " + r.toString());

            return r.toString().trim();
        }

        }
 }
