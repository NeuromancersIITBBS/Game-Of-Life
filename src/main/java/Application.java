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
    JButton b[][];

    ArrayList<int[]> people = new ArrayList();
    ArrayList<int[]> kings = new ArrayList();

    public static void main(String[] args) {
        Application obj = new Application();
    }

    private Application() {
        super("Conway's Game of Life");
        setSize(800, 900);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        jPanel.setLayout(new BorderLayout());
        jPanel1.setLayout(new GridLayout(side, side, 0, 0));
        jPanel1.setPreferredSize(new Dimension(800, 800));

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

        jPanel2.setPreferredSize(new Dimension(800, 100));

        jPanel.add(jPanel1, BorderLayout.NORTH);
        jPanel.add(jPanel2, BorderLayout.SOUTH);
        add(jPanel);

        setVisible(true);
        System.out.println("Running");
    }

    private void init() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                b[i][j].removeActionListener(this);
                if (b[i][j].getBackground() == Color.BLACK) {
                    int temp[] = {i, j};
                    people.add(temp);
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
        if (n == 3 && !listContains(kings, temp)) {
            kings.add(temp);
        }
    }

    private void play() {
        while (!people.isEmpty()) {

            kings.clear();
            int n = 0;
            int x = 0, y = 0;
            for (int i = 0; i < people.size(); i++) {
                x = people.get(i)[0];
                y = people.get(i)[1];
                n = neighbours(x, y);
                if (n == 2 || n == 3) {
                    kings.add(people.get(i));
                }

                newLife(x, y);
            }
            people.clear();
            people = (ArrayList<int[]>) kings.clone();

            int temp[] = {0, 0};
            for (int i = 0; i < side; i++) {
                for (int j = 0; j < side; j++) {
                    temp[0] = i;
                    temp[1] = j;
                    if (listContains(kings, temp)) {
                        b[i][j].setBackground(Color.BLACK);
                    } else {
                        b[i][j].setBackground(Color.WHITE);
                    }
                }
            }

            break;
        }
    }

    private boolean listContains(ArrayList<int[]> list, int[] query) {
        for (int[] arr : kings) {
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
