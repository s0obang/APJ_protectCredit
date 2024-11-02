package panels;

import javax.swing.*;

import Manager.GameManager;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GamePanel extends JPanel {
    private final Random random = new Random(); // 무작위 생성기
    private final int iconSize = 15; // 아이콘의 크기
    private final ArrayList<FallingIcon> icons = new ArrayList<>(); // 떨어지는 아이콘들을 저장하는 리스트
    //private final String[] iconPaths = { "src/main/java/org.example/img/gradeItem/F.png", "src/main/java/org.example/img/gradeItem/A+.png" };
    // 절대 경로로 변경
    private final String[] iconPaths = {
            new File("").getAbsolutePath() + "/src/main/java/org/example/img/gradeItem/F.png",
            new File("").getAbsolutePath() + "/src/main/java/org/example/img/gradeItem/A+.png"
    };
    public GamePanel(GameManager manager) {

        setLayout(new BorderLayout());
        JButton endButton = new JButton("End Game (Game Over)");
        endButton.addActionListener(e -> manager.showEndScreen(false)); // 게임 오버로 종료 화면으로 이동
        JButton successButton = new JButton("End Game (Success)");
        successButton.addActionListener(e -> manager.showEndScreen(true)); // 성공으로 종료 화면으로 이동
        add(endButton, BorderLayout.WEST);
        add(successButton, BorderLayout.EAST);

        //어떻게 넘어가는지 감잡으슈 이해했으면 지워도 됨
        //게임 종료 화면으로 전환 시 boolean 값으로 게임 오버인지 졸업인지 알려줘야함
        //true -> 졸업, false -> 실패
        setPreferredSize(new Dimension(1080, 720));
        setOpaque(true);

        // 이미지 경로 확인 로그
        for (String path : iconPaths) {
            System.out.println("현재 이미지 경로: " + path);
            File file = new File(path);
            if (file.exists()) {
                System.out.println("파일 존재함: " + path);
            } else {
                System.out.println("파일이 존재하지 않음: " + path);
            }
        }

        // 타이머 설정 (30밀리초마다 업데이트)
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateIcons(); // 아이콘 위치 업데이트
                repaint(); // 화면 다시 그리기
            }
        });
        timer.start(); // 타이머 시작

    }



    // 떨어지는 아이콘을 나타내는 내부 클래스
    private class FallingIcon {
        int x, y, speed; // 아이콘의 위치와 속도
        Image image; // 아이콘 이미지

        FallingIcon(int x, int y) {
            this.x = x;
            this.y = y;
            this.speed = random.nextInt(2) + 1; // 1~3 사이의 랜덤 속도

            // 무작위로 아이콘 이미지 선택
            String iconPath = iconPaths[random.nextInt(iconPaths.length)];

            File imageFile = new File(iconPath);

            // 파일 존재 여부 확인 후 이미지 로드
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(iconPath);
                this.image = icon.getImage();

                // 이미지 로드 확인
                if (this.image.getWidth(null) <= 0) {
                    System.err.println("이미지 로드 실패 (파일은 존재하지만 이미지로 로드할 수 없음): " + iconPath);
                } else {
                    System.out.println("이미지 로드 성공: " + iconPath);
                }
            } else {
                System.err.println("파일을 찾을 수 없음: " + iconPath);
                // 이미지를 찾을 수 없을 경우 기본 이미지 생성
                this.image = createDefaultImage();
            }
        }
    }
    // 이미지를 찾을 수 없을 경우 사용할 기본 이미지 생성
    private Image createDefaultImage() {
        BufferedImage defaultImage = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultImage.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, iconSize, iconSize);
        g2d.setColor(Color.WHITE);
        g2d.drawString("?", iconSize/2-5, iconSize/2+5);
        g2d.dispose();
        return defaultImage;
    }

    // 패널에 아이콘을 그리는 메서드
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 부모 패널 초기화

        // 배경색을 설정
        g.setColor(Color.LIGHT_GRAY); // 배경색을 연한 회색으로 설정
        g.fillRect(0, 0, getWidth(), getHeight()); // 패널 전체에 색칠

        // 각 아이콘 그리기
        for (FallingIcon icon : icons) {
            g.drawImage(icon.image, icon.x, icon.y, iconSize, iconSize, this);
        }
    }

    // 아이콘의 위치와 리스트를 업데이트하는 메서드
    private void updateIcons() {
        // 무작위 x 위치에서 새로운 아이콘 추가 (10번 중 1번 확률로 추가)
        if (random.nextInt(10) == 0) {
            icons.add(new FallingIcon(random.nextInt(getWidth() - iconSize), -iconSize));
        }

        // 아이콘 위치 업데이트
        Iterator<FallingIcon> iterator = icons.iterator();
        while (iterator.hasNext()) {
            FallingIcon icon = iterator.next();
            icon.y += icon.speed; // 속도에 따라 아래로 이동

            // 화면 아래로 벗어난 아이콘 제거
            if (icon.y > getHeight()) {
                iterator.remove();
            }
        }
    }
}
