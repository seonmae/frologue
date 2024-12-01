package com.banuh.frologue.game.scenes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class waitingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 창 크기
        int width = 900;
        int height = 600;

        // Pane 레이아웃 생성
        Pane layout = new Pane();

        // Canvas 생성
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false); // 이미지 확대 시 보간 비활성화

        // 배경 이미지 그리기
        Image waitingImage = new Image("file:src/main/resources/img/background/waiting-room.png");
        gc.drawImage(waitingImage, 0, 0, width, height);

        // Start 버튼 이미지
        Image startButtonImage = new Image("file:src/main/resources/img/ui/start-button.png");
        double startButtonWidth = 200;
        double startButtonHeight = 200;
        double startButtonX = (width - startButtonWidth) / 2; // 정중앙
        double startButtonY = 400; // 하단 여백
        gc.drawImage(startButtonImage, startButtonX, startButtonY, startButtonWidth, startButtonHeight);

        // Character Select 버튼 이미지
        Image characterSelectImage = new Image("file:src/main/resources/img/ui/charecter-select-button.png");
        double characterSelectWidth = 170;
        double characterSelectHeight = 120;
        double characterSelectX = 50; // 오른쪽 여백
        double characterSelectY = 480; // 하단 여백
        gc.drawImage(characterSelectImage, characterSelectX, characterSelectY, characterSelectWidth, characterSelectHeight);

        // Canvas 추가
        layout.getChildren().add(canvas);

        // Character Select 버튼 클릭 이벤트 (투명한 클릭 영역 처리)
        Pane transparentButton = new Pane();
        transparentButton.setPrefSize(characterSelectWidth, characterSelectHeight);
        transparentButton.setLayoutX(characterSelectX);
        transparentButton.setLayoutY(characterSelectY);
        transparentButton.setOnMouseClicked(event -> openCharacterSelectModal());
        layout.getChildren().add(transparentButton);

        // Scene 생성
        Scene scene = new Scene(layout, width, height);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Waiting Page");
        primaryStage.show();
    }

    // 모달 창 열기 메서드
    private void openCharacterSelectModal() {
        // 모달 창 Stage 생성
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL); // 모달 창 설정
        modalStage.initStyle(javafx.stage.StageStyle.TRANSPARENT); // 모달 창 투명 배경 설정
        modalStage.setTitle("Character Select");

        // 모달 창 크기
        int modalWidth = 750;
        int modalHeight = 450;

        // Canvas 생성
        Canvas modalCanvas = new Canvas(modalWidth, modalHeight);
        GraphicsContext modalGc = modalCanvas.getGraphicsContext2D();
        modalGc.setImageSmoothing(false); // 이미지 확대 시 보간 비활성화

        // SelectEx 이미지 그리기
        Image selectExImage = new Image("file:src/main/resources/img/ui/selectEx.png");
        modalGc.drawImage(selectExImage, 0, 0, modalWidth, modalHeight);

        // 닫기 버튼 이미지 및 위치 설정
        Image closeImage = new Image("file:src/main/resources/img/ui/close.png");
        double closeButtonWidth = 25;
        double closeButtonHeight = 25;
        double closeButtonX = 20; // 왼쪽 위 (여백 20)
        double closeButtonY = 20;

        // 닫기 버튼 그리기
        modalGc.drawImage(closeImage, closeButtonX, closeButtonY, closeButtonWidth, closeButtonHeight);

        // 닫기 버튼 클릭 이벤트 처리
        modalCanvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // 닫기 버튼 클릭 확인
            if (mouseX >= closeButtonX && mouseX <= closeButtonX + closeButtonWidth &&
                    mouseY >= closeButtonY && mouseY <= closeButtonY + closeButtonHeight) {
                modalStage.close(); // 모달 창 닫기
            }
        });

        // 레이아웃
        Pane modalLayout = new Pane();
        modalLayout.getChildren().add(modalCanvas);

        // Scene 생성 및 배경 투명 설정
        Scene modalScene = new Scene(modalLayout, modalWidth, modalHeight);
        modalScene.setFill(javafx.scene.paint.Color.TRANSPARENT); // Scene 배경 투명 설정
        modalStage.setScene(modalScene);

        // 모달 창 표시
        modalStage.showAndWait();
    }

}