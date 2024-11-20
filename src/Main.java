import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private int score = 0;
    private int goal = 0;
    private int multiplier = 1;  // Starting multiplier
    private int multiplierCost = 5;  // Starting cost for multiplier
    private boolean powerUpEnabled = false;  // Power-up state
    private int difficultyMultiplier = 1;  // Difficulty level multiplier

    @Override
    public void start(Stage primaryStage) {
        // Layouts
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        HBox scoreLayout = new HBox(10);

        // Controls
        // Text Control (Display Score)
        Text scoreText = new Text("Score: 0");

        // Label Control (Instruction)
        Label instructionLabel = new Label("Click the cookie to increase your score!");

        // TextField Control (Username Entry) with Enter Key
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your name");

        // Label to display the name after submission
        Label nameDisplayLabel = new Label("Your name: ");

        // ProgressBar Control (Display progress towards goal)
        ProgressBar progressBar = new ProgressBar(0);

        // TextField Control (Goal Entry) with Enter Key
        TextField goalField = new TextField();
        goalField.setPromptText("Set a cookie goal:");

        // Label to display the name after submission
        Label cookieGoal = new Label("Your cookie goal: ");

        // Set Enter key action for TextField
        usernameField.setOnAction(e -> {
            String name = usernameField.getText();
            if (!name.isEmpty()) {
                nameDisplayLabel.setText("Your name: " + name);
            } else {
                nameDisplayLabel.setText("Please enter a valid name.");
            }
        });

        // ChoiceBox Control (Select Difficulty)
        ChoiceBox<String> difficultyChoice = new ChoiceBox<>();
        difficultyChoice.getItems().addAll("Easy", "Medium", "Hard");
        difficultyChoice.setValue("Easy");
        difficultyChoice.setOnAction(e -> {
            String difficulty = difficultyChoice.getValue();
            switch (difficulty) {
                case "Easy":
                    difficultyMultiplier = 1;
                    break;
                case "Medium":
                    difficultyMultiplier = 2/3;
                    break;
                case "Hard":
                    difficultyMultiplier = 1/3;
                    break;
            }
            instructionLabel.setText("Difficulty set to: " + difficulty);
        });

        // Set Enter key action for TextField
        goalField.setOnAction(e -> {
             goal = Integer.parseInt(goalField.getText());
            if (goal != 0) {
                cookieGoal.setText("Your goal: " + goal);
            } else {
                cookieGoal.setText("Set a goal");
            }
            progressBar.setProgress((double) score / goal);
        });

        // ImageView Control (Clickable Cookie Image)
        ImageView cookieImage = new ImageView(new Image("Cookie.png"));
        cookieImage.setFitHeight(100);
        cookieImage.setFitWidth(100);

        // Set onMouseClicked event to increase score when clicking the cookie image
        cookieImage.setOnMouseClicked(e -> {
            int increment = multiplier * difficultyMultiplier;
            if (powerUpEnabled) {
                increment += 5;  // Bonus cookies when power-up is enabled
            }
            score += increment;

            scoreText.setText("Score: " + score);

            progressBar.setProgress((double) score / goal);

        });

        // Multiplier Button
        Button multiplierButton = new Button("Increase Multiplier (Cost: " + multiplierCost + " Cookies)");
        multiplierButton.setOnAction(e -> {
            if (score >= multiplierCost) {
                score -= multiplierCost;
                multiplier += 1;  // Increase the multiplier by 1x
                multiplierCost *= 2;  // Double the cost for each multiplier upgrade
                scoreText.setText("Score: " + score);
                multiplierButton.setText("Multiplier: " + multiplier + "x (Cost: " + multiplierCost + " Cookies)");
                progressBar.setProgress((double) score / goal);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Not enough cookies!");
                alert.showAndWait();
            }
        });

        // CheckBox Control (Enable Power-Ups)
        CheckBox powerUpCheckBox = new CheckBox("Enable Power-Ups");
        powerUpCheckBox.setOnAction(e -> {
            powerUpEnabled = powerUpCheckBox.isSelected();
            if (powerUpEnabled) {
                instructionLabel.setText("Power-Up enabled: Extra cookies per click!");
            } else {
                instructionLabel.setText("Power-Up disabled.");
            }
        });

        // ColorPicker Control (Background Color Picker)
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> mainLayout.setStyle("-fx-background-color: #" + colorPicker.getValue().toString().substring(2, 8) + ";"));



        // Slider Control (Adjust Goal)
        Slider goalSlider = new Slider(0, 100, 50);
        goalSlider.setShowTickLabels(true);
        goalSlider.setShowTickMarks(true);

        // Update the progress bar when the slider is moved or released
        goalSlider.setOnMouseReleased(e -> progressBar.setProgress((double) score / goalSlider.getValue()));

        // Alternatively, use a listener for real-time update as the slider moves
        goalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            progressBar.setProgress((double) score / newValue.doubleValue());
        });

        // Add Controls to Layouts
        scoreLayout.getChildren().addAll(scoreText, progressBar);
        mainLayout.getChildren().addAll(
                instructionLabel,
                usernameField,
                nameDisplayLabel,
                difficultyChoice,
                cookieImage,
                multiplierButton,
                scoreLayout,
                powerUpCheckBox,
                colorPicker,
                goalField,
                goalSlider
        );

        // Scene
        Scene scene = new Scene(mainLayout, 400, 500);
        primaryStage.setTitle("Cookie Clicker Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateDifficulty(String difficulty) {
        System.out.println("Difficulty set to: " + difficulty);
    }

    private void handlePowerUp(boolean isEnabled) {
        if (isEnabled) {
            System.out.println("Power-Ups enabled!");
        } else {
            System.out.println("Power-Ups disabled.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
