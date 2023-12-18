import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class examAndQuestion {
    private static final int NUM_QUESTIONS = 10;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = scanner.nextLine();
        System.out.println("Enter password");
        String password = scanner.nextLine();

        JSONParser parser = new JSONParser();
        try {
            JSONArray users = (JSONArray) parser.parse(new FileReader("./src/main/resources/Users.json"));
            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;
                if (username.equals(user.get("username")) && password.equals(user.get("password"))) {
                    if ("admin".equals(user.get("role"))) {
                        System.out.println("Welcome admin! Please create new questions in the question bank.");
                        addQuestions();
                    } else if ("student".equals(user.get("role"))) {
                        System.out.println("Welcome " + username + " to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' to start.");
                        startQuiz();
                    }
                    return;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Invalid username or password");
    }

    public static void addQuestions() {
        List<JSONObject> questions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            JSONObject questionObject = new JSONObject();

            System.out.println("System:> Input your question");
            String question = scanner.nextLine();
            questionObject.put("question", question);

            for (int i = 1; i <= 4; i++) {
                System.out.println("System: Input option " + i + ":");
                String option = scanner.nextLine();
                questionObject.put("option " + i, option);
            }

            System.out.println("System: What is the answer key?");
            int answerKey = scanner.nextInt();
            scanner.nextLine();
            questionObject.put("answerkey", answerKey);

            questions.add(questionObject);

            System.out.println("System:> Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
            choice = scanner.nextLine().charAt(0);
        } while (choice != 'q');

        try (FileWriter file = new FileWriter("./src/main/resources/quiz.json")) {
            JSONArray quizArray = new JSONArray();

            quizArray.addAll(questions);

            file.write(quizArray.toJSONString());
            System.out.println("Quiz data saved to quiz.json");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }


    public static void startQuiz() {

        Scanner scanner = new Scanner(System.in);

        try (FileReader reader = new FileReader("./src/main/resources/quiz.json")) {
            JSONParser parser = new JSONParser();
            JSONArray quizArray = (JSONArray) parser.parse(reader);

            System.out.println("System:> Welcome to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' for start.");
            char start = scanner.nextLine().charAt(0);

            if (start == 's') {
                int totalQuestions = Math.min(NUM_QUESTIONS, quizArray.size());
                int score = 0;

                List<Integer> questionIndices = new ArrayList<>();
                for (int i = 0; i < quizArray.size(); i++) {
                    questionIndices.add(i);
                }
                Collections.shuffle(questionIndices);

                for (int i = 0; i < totalQuestions; i++) {
                    int questionIndex = questionIndices.get(i);
                    JSONObject questionObject = (JSONObject) quizArray.get(questionIndex);

                    System.out.println("\n[Question " + (i + 1) + "] " + questionObject.get("question"));
                    for (int j = 1; j <= 4; j++) {
                        System.out.println(j + ". " + questionObject.get("option " + j));
                    }

                    int userAnswer = -1;
                    while (userAnswer < 1 || userAnswer > 4) {
                        System.out.print("\nYour answer (1-4): ");
                        try {
                            userAnswer = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number between 1 and 4.");
                        }
                    }

                    int answerKey = ((Long) questionObject.get("answerkey")).intValue();
                    if (userAnswer == answerKey) {
                        score++;
                    }
                }

                System.out.println("\nSystem:> Quiz finished!");
                System.out.println("Your score: " + score + " out of " + totalQuestions);

                String message;
                if (score >= 8) {
                    message = "Excellent! You have got " + score + " out of " + totalQuestions;
                } else if (score >= 5) {
                    message = "Good. You have got " + score + " out of " + totalQuestions;
                } else if (score >= 2) {
                    message = "Very poor! You have got " + score + " out of " + totalQuestions;
                } else {
                    message = "Very sorry you are failed. You have got " + score + " out of " + totalQuestions;
                }

                System.out.println("Result: " + message);

                System.out.println("Would you like to start again? Press 's' for start or 'q' for quit");
                char choice = scanner.nextLine().charAt(0);

                if (choice == 's') {
                    startQuiz();
                }
            } else {
                System.out.println("Quiz not started. Exiting...");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

}
