import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MatchThreeGame extends JFrame {
    private JLabel[][] tiles = new JLabel[6][6];
    private List<JLabel> selectedTiles = new ArrayList<>();
    private int score = 0;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private boolean gameEnded = false;
    private Random random = new Random();
    private Color selectedColor = Color.YELLOW;
    private Icon[] icons = new Icon[6];
    private BufferedImage backgroundImage;
    private BufferedImage startBackgroundImage;
    private BufferedImage victoryBackgroundImage;
    private BufferedImage defeatBackgroundImage;
    private Timer timer;
    private int timeLeft = 40; // 游戏时间为60秒

    public MatchThreeGame() {
        loadIcons();
        loadBackground();
        loadStartBackground();
        loadVictoryBackground();
        loadDefeatBackground();

        setTitle("Match Three Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        // 创建背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false);

        // 创建游戏面板
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(6, 6, 5, 5));
        gamePanel.setOpaque(false);

        // 初始化瓷砖
        initTiles(gamePanel);

        // 创建分数标签
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24)); // 放大字体
        scoreLabel.setOpaque(false);
        scoreLabel.setForeground(Color.RED);
        // 创建时间标签
        timeLabel = new JLabel("Time Left: " + timeLeft);
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24)); // 放大字体
        timeLabel.setOpaque(false);
        timeLabel.setForeground(Color.RED);
        // 创建计时器
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timeLabel.setText("Time Left: " + timeLeft);
                if (timeLeft <= 0) {
                    timer.stop();
                    gameEnded = true;
                    showDefeatScreen();
                }
            }
        });
        timer.start();

        // 添加到背景面板
        backgroundPanel.add(scoreLabel, BorderLayout.NORTH);
        backgroundPanel.add(timeLabel, BorderLayout.SOUTH); // 将时间标签放置在底部
        backgroundPanel.add(gamePanel, BorderLayout.CENTER);

        // 将背景面板添加到主面板
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);

        // 创建启动面板
        JPanel startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(startBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        startPanel.setLayout(new GridBagLayout()); // 使用GridBagLayout来精确控制组件的位置
        startPanel.setOpaque(false);

        // 创建欢迎信息标签
        JLabel welcomeLabel = new JLabel("Welcome to the Match Three Game!");
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30)); // 放大字体
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setOpaque(false);

        // 创建开始游戏按钮
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20)); // 放大字体
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 移除启动面板
                remove(startPanel);
                // 添加主面板并使可见
                add(mainPanel);
                validate();
                repaint();
            }
        });

        // 设置GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // 居中对齐

        // 添加欢迎标签
        gbc.insets = new Insets(0, 0, 100, 0); // 增加底部内边距
        startPanel.add(welcomeLabel, gbc);

        // 添加开始按钮
        gbc.insets = new Insets(0, 0, 20, 0); // 移除内边距
        startPanel.add(startButton, gbc);

        // 添加启动面板到窗口
        add(startPanel);

        // 显示窗口
        setVisible(true);
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/game_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load game background image.");
            setBackground(Color.LIGHT_GRAY);
        }
    }

    private void loadStartBackground() {
        try {
            startBackgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/start_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load start background image.");
            setBackground(Color.LIGHT_GRAY);
        }
    }

    private void loadVictoryBackground() {
        try {
            victoryBackgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/victory_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load victory background image.");
        }
    }

    private void loadDefeatBackground() {
        try {
            defeatBackgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/defeat_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load defeat background image.");
        }
    }

    private void loadIcons() {
        icons[0] = new ImageIcon("src/resources/tile1.jpg");
        icons[1] = new ImageIcon("src/resources/tile2.jpg");
        icons[2] = new ImageIcon("src/resources/tile3.jpg");
        icons[3] = new ImageIcon("src/resources/tile4.jpg");
        icons[4] = new ImageIcon("src/resources/tile5.jpg");
        icons[5] = new ImageIcon("src/resources/tile6.jpg");
    }

    private void initTiles(JPanel panel) {
        List<Integer> tileValues = generateBalancedTiles();
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                JLabel tile = new JLabel(icons[tileValues.get(index)]);
                tile.setOpaque(false);
                tile.setBorder(BorderFactory.createEmptyBorder());
                tile.addMouseListener(new TileMouseListener(tile));
                tile.setEnabled(true);
                panel.add(tile);
                tiles[i][j] = tile;
                index++;
            }
        }
    }

    private class TileMouseListener extends MouseAdapter {
        private final JLabel tile;

        public TileMouseListener(JLabel tile) {
            this.tile = tile;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (gameEnded || !tile.isEnabled()) return;

            if (!selectedTiles.contains(tile)) {
                selectedTiles.add(tile);
                tile.setBorder(BorderFactory.createLineBorder(selectedColor, 2));
                if (selectedTiles.size() == 3) {
                    if (isMatchingSelection(selectedTiles)) {
                        removeSelectedTiles();
                        updateScore();
                    } else {
                        JOptionPane.showMessageDialog(MatchThreeGame.this, "Please select three matching tiles.");

                        // 移除选定方块的边框
                        for (JLabel t : selectedTiles) {
                            t.setBorder(BorderFactory.createEmptyBorder());
                        }
                    }
                    selectedTiles.clear();
                }
            }
        }
    }

    private List<Integer> generateBalancedTiles() {
        List<Integer> tiles = new ArrayList<>();
        int[] counts = {6, 6, 6, 6, 6, 6};
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < counts[i]; j++) {
                tiles.add(i);
            }
        }
        Collections.shuffle(tiles, random);
        return tiles;
    }

    private boolean isMatchingSelection(List<JLabel> tiles) {
        if (tiles.size() != 3) return false;
        Icon icon = ((JLabel) tiles.get(0)).getIcon();
        for (JLabel t : tiles) {
            if (!((JLabel) t).getIcon().equals(icon)) {
                return false;
            }
        }
        return true;
    }

    private void removeSelectedTiles() {
        for (JLabel tile : selectedTiles) {
            tile.setIcon(null);
            tile.setBorder(BorderFactory.createEmptyBorder());
            tile.setEnabled(false);
        }
        checkIfGameOver();
    }

    private void updateScore() {
        score += 10;
        scoreLabel.setText("Score: " + score);
    }

    private void checkIfGameOver() {
        boolean allRemoved = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tiles[i][j].getIcon() != null) {
                    allRemoved = false;
                    break;
                }
            }
            if (!allRemoved) break;
        }
        if (allRemoved) {
            gameEnded = true;
            showVictoryScreen();
        }
    }

    private void showVictoryScreen() {
        // 隐藏游戏界面元素
        JPanel gamePanel = (JPanel) getContentPane();
        for (Component component : gamePanel.getComponents()) {
            component.setVisible(false);
        }

        // 显示胜利背景
        JPanel victoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(victoryBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        victoryPanel.setLayout(new BorderLayout());
        victoryPanel.setOpaque(false);
        gamePanel.add(victoryPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private void showDefeatScreen() {
        // 隐藏游戏界面元素
        JPanel gamePanel = (JPanel) getContentPane();
        for (Component component : gamePanel.getComponents()) {
            component.setVisible(false);
        }

        // 显示失败背景
        JPanel defeatPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(defeatBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        defeatPanel.setLayout(new BorderLayout());
        defeatPanel.setOpaque(false);
        gamePanel.add(defeatPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    public static void main(String[] args) {
        new MatchThreeGame();
    }
}