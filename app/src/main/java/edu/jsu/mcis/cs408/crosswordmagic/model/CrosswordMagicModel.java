package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.Context;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.DAOFactory;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.PuzzleDAO;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.WebServiceDAO;
import edu.jsu.mcis.cs408.crosswordmagic.view.CrosswordGridView;


public class CrosswordMagicModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;

    private Puzzle puzzle;

    private final DAOFactory daoFactory;
    private final  PuzzleDAO puzzleDAO;

    private final WebServiceDAO webServiceDAO;



    public CrosswordMagicModel(Context context, Integer id) {

        this.daoFactory = new DAOFactory(context);
        this.puzzleDAO = daoFactory.getPuzzleDAO();

        this.puzzle = puzzleDAO.find(id.intValue());//added plus one for offset
        this.webServiceDAO = daoFactory.getWebServiceDAO();


    }

    public void getTestProperty() {

        String wordCount = (this.puzzle != null ? String.valueOf(puzzle.getSize()) : "none");
        firePropertyChange(CrosswordMagicController.TEST_PROPERTY, null, wordCount);

    }
    public void getGridDimensions(){
        Integer[] dims = new Integer[2];
        dims[0] = puzzle.getHeight();
        dims[1] = puzzle.getWidth();
        firePropertyChange(CrosswordMagicController.GRID_DIMENSION_PROPERTY, null, dims);

    }
    public void getGridLetters(){

        firePropertyChange(CrosswordMagicController.GRID_LETTERS_PROPERTY, null, puzzle.getLetters());


    }
    public void getGridNumbers(){
        firePropertyChange(CrosswordMagicController.GRID_NUMBERS_PROPERTY, null, puzzle.getNumbers());
    }
    public void getClues(){
        String[] clues = {puzzle.getCluesAcross(), puzzle.getCluesDown()};
        firePropertyChange(CrosswordMagicController.CLUE_WORDS_PROPERTY, null, clues);
        
    }
    public void setGuess(String[] parcel){

        Character[][] copy = puzzle.getLetters();

        int number = Integer.parseInt(parcel[0]);
        if(puzzle.checkGuess(number, parcel[1]) != null){

             getGridLetters();
            firePropertyChange(CrosswordMagicController.GUESS_WORD_PROPERTY, null, true);

        }else{
            firePropertyChange(CrosswordMagicController.GUESS_WORD_PROPERTY, null, false);
        }





    }
    public void getPuzzleList(){


        PuzzleListItem[] PLI = puzzleDAO.list();
        if(PLI != null){
            firePropertyChange(CrosswordMagicController.PUZZLE_LIST_PROPERTY, null, PLI);
        }

    }

    public void getPuzzleGet(){

        ArrayList<PuzzleListItem> temp = webServiceDAO.list();
        firePropertyChange(CrosswordMagicController.PUZZLE_GET_REQUEST, null, temp);

    }
    public void setPuzzleAddToDatabaseHTTP(Integer id){
        Integer temp = webServiceDAO.addPuzzleByIDToDatabase(id.intValue());
        firePropertyChange(CrosswordMagicController.PUZZLE_ADD_TO_DATABASE_HTTP, null, temp);

    }

    public class WebServices {

        private static final String TAG = "WebService";

        private static final String GET_URL = "http://ec2-3-142-171-53.us-east-2.compute.amazonaws.com:8080/CrosswordMagicServer/puzzle?id=1";


        private JSONObject jsonData;

        private JSONObject outputInfo;

        private final ExecutorService requestThreadExecutor;
        private final Runnable httpGetRequestThread;
        private Future<?> pending;






        public WebServices() {

            requestThreadExecutor = Executors.newSingleThreadExecutor();

            httpGetRequestThread = new Runnable() {

                @Override
                public void run() {


                    /* Begin new request now, but don't wait for it */
                    if (pending != null) { pending.cancel(true); }

                    /* Begin new request now, but don't wait for it */

                    try {
                        pending = requestThreadExecutor.submit(new HTTPRequestTask("GET", GET_URL));
                    }
                    catch (Exception e) { Log.e(TAG, " Exception: ", e); }
                }

            };

        }

       public void killThreads(){
       }
        public JSONObject getOutputText() {
            return outputInfo;
        }

        public void setOutputText(JSONObject stuff) {

            JSONObject oldInfo = this.outputInfo;
            this.outputInfo = stuff;



            firePropertyChange(CrosswordMagicController.PUZZLE_GET_REQUEST, null, this.getJsonData());

        }

        // Start GET Request (called from Controller)

        public void sendGetRequest() {
            //requestThreadExecutor.execute(httpGetRequestThread);
            httpGetRequestThread.run();

            int y=1;

        }

        // Start POST Request (called from Controller)


        // Setter / Getter Methods for JSON LiveData

        private synchronized void setJsonData(JSONObject json) {

            if (json == null) {
                Log.w(TAG, "setJsonData: received null JSON. Replacing with empty array.");
                json = new JSONObject();
            }

            this.jsonData = json;

            try {
                setOutputText(json);
                int y =1;
            } catch (Exception e) {
                Log.e(TAG, "Error converting JSON to string. I HAVE BEEN MORTALLY WOUNDED", e);
            }

        }

        public JSONObject getJsonData() {
            if (jsonData == null) {
                jsonData = new JSONObject();
            }
            return jsonData;
        }

        // Private Class for HTTP Request Threads

        private class HTTPRequestTask implements Runnable {

            private static final String TAG = "HTTPRequestTask";
            private final String method, urlString;

            HTTPRequestTask(String method, String urlString) {
                this.method = method;
                this.urlString = urlString;
            }

            @Override
            public void run() {
                JSONObject results = doRequest(urlString);
                if (results == null) {
                    Log.e(TAG, "doRequest() returned null — something failed");
                } else {
                    Log.d(TAG, "doRequest() returned " + results.length() + " items");
                }

                setJsonData(results);
            }

            /* Create and Send Request */

            private JSONObject doRequest(String urlString) {
                if (Thread.currentThread().isInterrupted()) {
                    Log.e(TAG, "Thread was already interrupted before HTTP started");

                    // Optional: print stack trace of who might have done it
                    for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                        Log.e(TAG, "STACK TRACE: " + ste.toString());
                    }
                }

                StringBuilder r = new StringBuilder();
                String line;

                //HttpURLConnection conn = null;

                JSONObject results = null;
                HttpURLConnection conn = null;


                /* Log Request Data */

                try {


                    try {

                        if (Thread.interrupted()){
                            int y =1;
                            throw new InterruptedException();
                        }

                        // HTTP logic
                    } catch (InterruptedException e) {
                        Log.w("HTTPRequestTask", "Task was interrupted.");
                        return null;
                    }
                    /* Create Request */
                    int y =1;
                    URL url = new URL(urlString);
                    Log.w(TAG, "URL created");



                    conn = (HttpURLConnection)url.openConnection();

                    Log.w(TAG, "Opened connection");

                    conn.setReadTimeout(10000); /* ten seconds */
                    conn.setConnectTimeout(15000); /* fifteen seconds */

                    conn.setRequestMethod(method);
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

                return results;

            }

        }


    }

}