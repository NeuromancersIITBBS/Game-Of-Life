import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Application extends JFrame implements ActionListener {

    JPanel jPanel = new JPanel();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    int side = 40;
    int dimen = 800;
    JButton b[][];

    ArrayList<int[]> current = new ArrayList();
    ArrayList<int[]> next = new ArrayList();

    public static void main(String[] args) {
        new Application();
    }

    private Application() {
        super("Conway's Game of Life");
        setSize(dimen, dimen + 100);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        jPanel.setLayout(new BorderLayout());
        jPanel1.setLayout(new GridLayout(side, side, 0, 0));
        jPanel1.setPreferredSize(new Dimension(dimen, dimen));

        b = new JButton[side][side];

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                b[i][j] = new JButton();
                b[i][j].setBackground(Color.WHITE);
                b[i][j].addActionListener(this);
                jPanel1.add(b[i][j]);
            }
        }

        jPanel2.setLayout(new BorderLayout());
        JButton start = new JButton();
        start.setText("Start");
        JButton next = new JButton();
        next.setText("Next");
        jPanel2.add(next, BorderLayout.CENTER);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                play();
            }
        });
        jPanel2.add(start, BorderLayout.WEST);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                init();
            }
        });

        jPanel2.setPreferredSize(new Dimension(dimen, 100));

        jPanel.add(jPanel1, BorderLayout.NORTH);
        jPanel.add(jPanel2, BorderLayout.SOUTH);
        add(jPanel);

        setVisible(true);
    }

    private void init() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                b[i][j].removeActionListener(this);
                if (b[i][j].getBackground() == Color.BLACK) {
                    int temp[] = {i, j};
                    current.add(temp);
                }
            }
        }
    }

    private int neighbours(int i, int j) {
        int count = 0;
        if (i >= 0 && i < side && (j + 1) >= 0 && (j + 1) < side && b[i][j + 1].getBackground() == Color.BLACK) count++;
        if (i >= 0 && i < side && (j - 1) >= 0 && (j - 1) < side && b[i][j - 1].getBackground() == Color.BLACK) count++;
        if ((i + 1) >= 0 && (i + 1) < side && (j + 1) >= 0 && (j + 1) < side && b[i + 1][j + 1].getBackground() == Color.BLACK)
            count++;
        if ((i + 1) >= 0 && (i + 1) < side && (j - 1) >= 0 && (j - 1) < side && b[i + 1][j - 1].getBackground() == Color.BLACK)
            count++;
        if ((i - 1) >= 0 && (i - 1) < side && (j + 1) >= 0 && (j + 1) < side && b[i - 1][j + 1].getBackground() == Color.BLACK)
            count++;
        if ((i - 1) >= 0 && (i - 1) < side && (j - 1) >= 0 && (j - 1) < side && b[i - 1][j - 1].getBackground() == Color.BLACK)
            count++;
        if ((i + 1) >= 0 && (i + 1) < side && j >= 0 && j < side && b[i + 1][j].getBackground() == Color.BLACK) count++;
        if ((i - 1) >= 0 && (i - 1) < side && j >= 0 && j < side && b[i - 1][j].getBackground() == Color.BLACK) count++;

        return count;
    }

    private void newLife(int x, int y) {
        if (x >= 0 && x < side && (y + 1) >= 0 && (y + 1) < side && b[x][y + 1].getBackground() == Color.WHITE)
            enqueue(x, y + 1);
        if (x >= 0 && x < side && (y - 1) >= 0 && (y - 1) < side && b[x][y - 1].getBackground() == Color.WHITE)
            enqueue(x, y - 1);
        if ((x + 1) >= 0 && (x + 1) < side && (y + 1) >= 0 && (y + 1) < side && b[x + 1][y + 1].getBackground() == Color.WHITE)
            enqueue(x + 1, y + 1);
        if ((x + 1) >= 0 && (x + 1) < side && (y - 1) >= 0 && (y - 1) < side && b[x + 1][y - 1].getBackground() == Color.WHITE)
            enqueue(x + 1, y - 1);
        if ((x - 1) >= 0 && (x - 1) < side && (y + 1) >= 0 && (y + 1) < side && b[x - 1][y + 1].getBackground() == Color.WHITE)
            enqueue(x - 1, y + 1);
        if ((x - 1) >= 0 && (x - 1) < side && (y - 1) >= 0 && (y - 1) < side && b[x - 1][y - 1].getBackground() == Color.WHITE)
            enqueue(x - 1, y - 1);
        if ((x + 1) >= 0 && (x + 1) < side && y >= 0 && y < side && b[x + 1][y].getBackground() == Color.WHITE)
            enqueue(x + 1, y);
        if ((x - 1) >= 0 && (x - 1) < side && y >= 0 && y < side && b[x - 1][y].getBackground() == Color.WHITE)
            enqueue(x - 1, y);
    }

    private void enqueue(int p, int q) {
        int n = neighbours(p, q);
        int temp[] = {p, q};
        if (n == 3 && !listContains(temp)) {
            next.add(temp);
        }
    }

    private void play() {
        while (!current.isEmpty()) {

            next.clear();
            int n = 0;
            int x = 0, y = 0;
            for (int i = 0; i < current.size(); i++) {
                x = current.get(i)[0];
                y = current.get(i)[1];
                n = neighbours(x, y);
                if (n == 2 || n == 3) {
                    next.add(current.get(i));
                }

                newLife(x, y);
            }
            current.clear();
            current = (ArrayList<int[]>) next.clone();

            int temp[] = {0, 0};
            for (int i = 0; i < side; i++) {
                for (int j = 0; j < side; j++) {
                    temp[0] = i;
                    temp[1] = j;
                    if (listContains(temp)) {
                        b[i][j].setBackground(Color.BLACK);
                    } else {
                        b[i][j].setBackground(Color.WHITE);
                    }
                }
            }

            break;
        }
    }

    private boolean listContains(int[] query) {
        for (int[] arr : next) {
            if (arr[0] == query[0] && arr[1] == query[1]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton temp = ((JButton) (actionEvent.getSource()));
        if (temp.getBackground() == Color.WHITE) temp.setBackground(Color.BLACK);
        else temp.setBackground(Color.WHITE);
    }
}
