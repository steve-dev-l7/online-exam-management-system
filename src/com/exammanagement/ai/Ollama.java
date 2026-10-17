package com.exammanagement.ai;

import com.exammanagement.model.Questions;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ollama {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "qwen3:8b"; 

    

    // =========================================================================
    // METHOD 1: EXAM GENERATION (Returns List of Questions)
    // =========================================================================
    public static List<Questions> generateExam(String subject, String topic, int numberOfQuestions) throws Exception {
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic must not be blank.");
        }
        if (numberOfQuestions < 5) {
            throw new IllegalArgumentException("Number of questions must be greater than five.");
        }

        
        String examPrompt = "You are an expert exam generator. " +
    "You MUST generate EXACTLY " + numberOfQuestions + " multiple-choice questions for the subject: '" + subject + "', topic: '" + topic + "'. " +
    "CRITICAL RULE: You must output a JSON object containing the exact count and the array of questions. Do not stop early. " +
    "Return ONLY valid JSON in this exact format: " +
    "{\"total_questions\": " + numberOfQuestions + ", \"questions\": [{\"question\": \"...\", \"options\": [\"...\", \"...\", \"...\", \"...\"], \"answer\": \"...\"}]}";
        List<Questions> questionList = new ArrayList<>();
        HttpURLConnection con = null;
        
        try {
            URL url = URI.create(OLLAMA_URL).toURL();
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setConnectTimeout(10_000);
            con.setReadTimeout(120_000);
            con.setDoOutput(true);

            // Namma munnadi pesina "Lazy Fix" options-a thirumba add pannirukken (for strict 10 questions)
            JSONObject modelOptions = new JSONObject()
                    .put("temperature", 0.3)
                    .put("num_predict", 4000);

            JSONObject request = new JSONObject()
                    .put("model", MODEL_NAME)
                    .put("prompt", examPrompt) // Passing the dynamic prompt here
                    .put("format", "json")
                    .put("stream", false)
                    .put("options", modelOptions);

            try(OutputStream os = con.getOutputStream()){
                byte[] input = request.toString().getBytes("utf-8");
                os.write(input, 0 ,input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder responseString = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseString.append(responseLine.trim());
            }

            JSONObject jsonResponse = new JSONObject(responseString.toString());
            String aiOutput = jsonResponse.getString("response");

            
            // --- THE MASTER FIX: Extract JSON Object instead of Array ---
            int startIndex = aiOutput.indexOf('{'); // Bracket maari irukku kavanikkavum
            int endIndex = aiOutput.lastIndexOf('}');
            
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                aiOutput = aiOutput.substring(startIndex, endIndex + 1);
            } else {
                throw new Exception("Valid JSON Object not found in AI output!");
            }
            // --------------------------------------------------------------------------

            // Ippo root object-a parse pandrom
            JSONObject rootJson = new JSONObject(aiOutput);
            
            // Athulla irunthu 'questions' array-va mattum edukurom
            JSONArray questionsArray = rootJson.getJSONArray("questions");

            // 7. Looping through JSON and adding to our Java Model
            for (int i = 0; i < questionsArray.length(); i++) {
                // ... (Unga pazhaya loop code apdiye irukkattum)
                JSONObject qObj = questionsArray.getJSONObject(i);
                
                Questions q = new Questions();
                q.setQuestionText(qObj.getString("question"));
                
                JSONArray options = qObj.getJSONArray("options");
                q.setOptA(options.getString(0));
                q.setOptB(options.getString(1));
                q.setOptC(options.getString(2));
                q.setOptD(options.getString(3));
                
                q.setCorrectAnswer(qObj.getString("answer"));
                
                questionList.add(q);
            }
            System.out.println("[AI] Successfully generated " + questionList.size() + " questions!");

        } catch (JSONException e) {
            System.out.println("Ollama connection error : " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
        return questionList;
    }

    // =========================================================================
    // METHOD 2: RESULT EXPLANATION (For Result Panel)
    // =========================================================================
    public static String getAnswerExplanation(String question, String wrongAnswer, String correctAnswer) {
        
        // Result panel-kku thevaiyana puthu prompt
        String explanationPrompt = "You are an expert tutor. A student answered a question incorrectly. " +
            "Question: '" + question + "'. " +
            "Student's wrong answer: '" + wrongAnswer + "'. " +
            "Correct answer: '" + correctAnswer + "'. " +
            "Explain briefly (in 2-3 sentences) why the correct answer is right and the student's answer is wrong. " +
            "Return ONLY a JSON object. Format: {\"explanation\": \"...\"}";

        HttpURLConnection con = null;
        try {
            URL url = URI.create(OLLAMA_URL).toURL();
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            JSONObject request = new JSONObject()
                    .put("model", MODEL_NAME)
                    .put("prompt", explanationPrompt) // Using the new prompt
                    .put("format", "json")
                    .put("stream", false);

            try(OutputStream os = con.getOutputStream()){
                byte[] input = request.toString().getBytes("utf-8");
                os.write(input, 0 ,input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder responseString = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseString.append(responseLine.trim());
            }

            JSONObject jsonResponse = new JSONObject(responseString.toString());
            String aiOutput = jsonResponse.getString("response");

            // Inga simple JSONObject parsing thaan, Array parsing thevai illa
            JSONObject explanationJson = new JSONObject(aiOutput);
            return explanationJson.getString("explanation");

        } catch (Exception e) {
            System.out.println("Ollama Explanation Error: " + e.getMessage());
            return "Unable to generate explanation at this time.";
        } finally {
            if (con != null) con.disconnect();
        }
    }

    // =========================================================================
    // METHOD 3: GENERAL CHATBOT
    // =========================================================================
    public static String getGeneralChatResponse(String userQuery) {
        String prompt = "You are a friendly AI programming tutor. Answer the student's question concisely: " + userQuery;
        
        HttpURLConnection con = null;
        try {
            java.net.URL url = java.net.URI.create(OLLAMA_URL).toURL();
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Notice: format="json" is REMOVED here because we just want pure text back
            org.json.JSONObject request = new org.json.JSONObject()
                    .put("model", MODEL_NAME)
                    .put("prompt", prompt)
                    .put("stream", false);

            try(OutputStream os = con.getOutputStream()){
                byte[] input = request.toString().getBytes("utf-8");
                os.write(input, 0 ,input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder responseString = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseString.append(responseLine.trim());
            }

            org.json.JSONObject jsonResponse = new org.json.JSONObject(responseString.toString());
            return jsonResponse.getString("response");

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I am offline right now.";
        } finally {
            if (con != null) con.disconnect();
        }
    }
}