package com.banuh.frologue.game.scenes;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import com.banuh.frologue.App;

public class StartPage extends Application {
    private double animationTimeJumpFrog = 0; // 개구리 움직임을 위한 타이밍 변수
    private boolean isHoveringStart = false; // 버튼 상호작용을 위해
    private boolean isHoveringExplanation = false;
    private boolean isHoveringExit = false;
    private MediaPlayer mediaPlayer; // 배경 음악을 위한 객체
    private Stage settingsStage = null; // 설정창

    private double soundVolume = 0.5; // 효과음 기본 설정 (50%)
    private double musicVolume = 0.5; // 배경음악 기본 설정 (50%)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("개구리다");

        int canvasWidth = 900;
        int canvasHeight = 600;

        loadSettings();
        initializeBackgroundMusic(); // 배경음악 초기화
        startBackgroundMusic(); // 배경음악 시작

        // 창 닫힐 때 배경음악 정지
        primaryStage.setOnCloseRequest(event -> stopBackgroundMusic());

        Image wallpaper1 = new Image("file:src/main/resources/img/start-page/main_wallpaper1.png"); // 배경1
        Image wallpaper2 = new Image("file:src/main/resources/img/start-page/main_wallpaper2.png"); // 배경2
        Image teamNameImage = new Image("file:src/main/resources/img/start-page/team_name.png"); // 별것도아닌데어금지
        Image mainTitleImage = new Image("file:src/main/resources/img/start-page/game_title.png"); // 개구리다
        Image jumpFrogImage = new Image("file:src/main/resources/img/frog-normal/jump.png"); // 점프 개구리
        Image startButtonImage = new Image("file:src/main/resources/img/start-page/main_game_start.png"); // 게임 시작 버튼
        Image explanationButtonImage = new Image("file:src/main/resources/img/start-page/main_game_explanation.png"); // 게임 설명 버튼
        Image exitButtonImage = new Image("file:src/main/resources/img/start-page/main_game_end.png"); // 게임 종료 버튼

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        final double[] wallpaper1X = {0};
        final double[] wallpaper2X = {0};

        // 별것도아닌데어금지 이미지 크기와 위치 설정
        double teamNameScaledWidth = teamNameImage.getWidth() * 0.2;
        double teamNameScaledHeight = teamNameImage.getHeight() * 0.2;
        double teamNameX = (canvasWidth - teamNameScaledWidth) / 2;
        double teamNameY = 50;

        // 개구리다 이미지 크기와 위치 설정
        double mainTitleScaledWidth = mainTitleImage.getWidth() * 0.3;
        double mainTitleScaledHeight = mainTitleImage.getHeight() * 0.3;
        double mainTitleX = (canvasWidth - mainTitleScaledWidth) / 2;
        double mainTitleY = teamNameY + teamNameScaledHeight + 25;

        // 점프 개구리 크기와 위치 설정
        double jumpFrogScaledWidth = jumpFrogImage.getWidth() * 6;
        double jumpFrogScaledHeight = jumpFrogImage.getHeight() * 6;
        double jumpFrogX = (canvasWidth - jumpFrogScaledWidth) / 2;
        double baseJumpFrogY = canvasHeight / 4;

        // 버튼 크기와 위치 설정
        double buttonScaleFactor = 0.1;
        double buttonWidth = startButtonImage.getWidth() * buttonScaleFactor;
        double buttonHeight = startButtonImage.getHeight() * buttonScaleFactor;

        double buttonY = 480;
        double buttonGap = 50;

        // 버튼 위치
        double buttonX1 = (canvasWidth - (buttonWidth * 3 + buttonGap * 2)) / 2;
        double buttonX2 = buttonX1 + buttonWidth + buttonGap;
        double buttonX3 = buttonX2 + buttonWidth + buttonGap;

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            isHoveringStart = isMouseHovering(mouseX, mouseY, buttonX1, buttonY, buttonWidth, buttonHeight);
            isHoveringExplanation = isMouseHovering(mouseX, mouseY, buttonX2, buttonY, buttonWidth, buttonHeight);
            isHoveringExit = isMouseHovering(mouseX, mouseY, buttonX3, buttonY, buttonWidth, buttonHeight);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            if (isHoveringStart) {
                System.out.println("게임 시작 버튼 클릭됨");
                playButtonSound(); // 효과음
                stopBackgroundMusic(); // 배경음악 중지
                try {
                    new Thread(() -> Application.launch(App.class)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (isHoveringExplanation) {
                System.out.println("게임 설명 버튼 클릭됨");
                playButtonSound();
                toggleSettingsWindow(); // 설정창 호출
            }

            if (isHoveringExit) {
                System.out.println("게임 종료 버튼 클릭됨");
                playButtonSound();
                primaryStage.close();
                stopBackgroundMusic(); // 배경음악 중지
                saveSettings(); // 설정 저장
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                animationTimeJumpFrog += 0.03;

                double srcWidth = 2700;
                double srcHeight = 1800;

                wallpaper1X[0] += 1;
                wallpaper2X[0] += 2;

                if (wallpaper1X[0] <= -srcWidth) {
                    wallpaper1X[0] += srcWidth;
                }

                if (wallpaper2X[0] <= -srcWidth) {
                    wallpaper2X[0] += srcWidth;
                }

                gc.clearRect(0, 0, canvasWidth, canvasHeight);

                gc.drawImage(wallpaper1, 0 + wallpaper1X[0], 0, srcWidth, srcHeight, 0, 0, 900, 600);
                gc.drawImage(wallpaper2, 0 + wallpaper2X[0], 0, srcWidth, srcHeight, 0, 0, 900, 600);

                gc.drawImage(teamNameImage, teamNameX, teamNameY, teamNameScaledWidth, teamNameScaledHeight);
                gc.drawImage(mainTitleImage, mainTitleX, mainTitleY, mainTitleScaledWidth, mainTitleScaledHeight);

                double jumpFrogOffsetY = Math.sin(animationTimeJumpFrog) * 8;
                gc.drawImage(jumpFrogImage, jumpFrogX, baseJumpFrogY + jumpFrogOffsetY, jumpFrogScaledWidth, jumpFrogScaledHeight);

                drawButton(gc, startButtonImage, buttonX1, buttonY, buttonWidth, buttonHeight, isHoveringStart);
                drawButton(gc, explanationButtonImage, buttonX2, buttonY, buttonWidth, buttonHeight, isHoveringExplanation);
                drawButton(gc, exitButtonImage, buttonX3, buttonY, buttonWidth, buttonHeight, isHoveringExit);
            }
        }.start();

        Pane layout = new Pane();
        layout.getChildren().add(canvas);
        primaryStage.setScene(new Scene(layout, canvasWidth, canvasHeight));
        primaryStage.show();
    }
    private void toggleSettingsWindow() {
        if (settingsStage == null || !settingsStage.isShowing()) {
            settingsStage = new Stage();
            settingsStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            // 모달 창 설정
            settingsStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // 레이아웃 생성
            Pane layout = new Pane();
            layout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

            // 배경 이미지 로드 및 설정
            Image backgroundImage = new Image("file:src/main/resources/img/start-page/explanatioin_sign.png");
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(400);
            backgroundView.setFitHeight(250);

            // 배경 이미지를 레이아웃에 추가
            layout.getChildren().add(backgroundView);

            // 효과음 레이블 생성 및 설정
            Label soundLabel = new Label("Sound effect 효과음:");
            soundLabel.setLayoutX(30);
            soundLabel.setLayoutY(30);
            soundLabel.setFont(new Font("Arial", 18));
            soundLabel.setTextFill(Color.BLACK);

            // 효과음 슬라이더 생성 및 설정
            Slider soundSlider = new Slider(0, 100, soundVolume * 100);
            soundSlider.setLayoutX(30);
            soundSlider.setLayoutY(60);
            soundSlider.setPrefWidth(340);
            soundSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                soundVolume = newVal.doubleValue() / 100.0;
                playButtonSound();
            });

            // 배경음악 레이블 생성 및 설정
            Label musicLabel = new Label("Music 배경음악:");
            musicLabel.setLayoutX(30);
            musicLabel.setLayoutY(100);
            musicLabel.setFont(new Font("Arial", 18));
            musicLabel.setTextFill(Color.BLACK);

            // 배경음악 슬라이더 생성 및 설정
            Slider musicSlider = new Slider(0, 100, musicVolume * 100);
            musicSlider.setLayoutX(30);
            musicSlider.setLayoutY(130);
            musicSlider.setPrefWidth(340);
            musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                musicVolume = newVal.doubleValue() / 100.0;
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(musicVolume);
                }
            });

            // 취소 버튼 생성 및 설정
            Button cancelButton = new Button("CANCEL");
            cancelButton.setLayoutX(165);
            cancelButton.setLayoutY(180);
            cancelButton.setOnAction(e -> settingsStage.close());

            // 레이아웃에 컴포넌트 추가
            layout.getChildren().addAll(soundLabel, soundSlider, musicLabel, musicSlider, cancelButton);

            // 투명 배경 설정
            Scene scene = new Scene(layout, 400, 250);
            scene.setFill(Color.TRANSPARENT);

            settingsStage.setScene(scene);
            settingsStage.show();
        } else {
            settingsStage.close();
            settingsStage = null;
        }
    }

    private boolean isMouseHovering(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void drawButton(GraphicsContext gc, Image buttonImage, double x, double y, double width, double height, boolean isHovering) {
        if (isHovering) {
            gc.drawImage(buttonImage, x - 2, y - 2, width + 4, height + 4);
        } else {
            gc.drawImage(buttonImage, x, y, width, height);
        }
    }

    private void playButtonSound() {
        try {
            String soundFile = "src/main/resources/music/main_button_sound.mp3";
            Media buttonSound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer soundPlayer = new MediaPlayer(buttonSound);
            soundPlayer.setVolume(soundVolume);
            soundPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeBackgroundMusic() {
        try {
            String musicFile = "src/main/resources/sound/music/frog_main_song.mp3";
            Media backgroundMusic = new Media(new File(musicFile).toURI().toString());
            mediaPlayer = new MediaPlayer(backgroundMusic);
            mediaPlayer.setVolume(musicVolume);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    private void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void loadSettings() {
        try {
            File configFile = new File("src/main/resources/config.properties");
            if (configFile.exists()) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(configFile));
                soundVolume = Double.parseDouble(properties.getProperty("soundVolume", "0.5"));
                musicVolume = Double.parseDouble(properties.getProperty("musicVolume", "0.5"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSettings() {
        try {
            Properties properties = new Properties();
            properties.setProperty("soundVolume", String.valueOf(soundVolume));
            properties.setProperty("musicVolume", String.valueOf(musicVolume));
            properties.store(new FileOutputStream("src/main/resources/config.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
