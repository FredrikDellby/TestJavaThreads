package MinerProject;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JPanel;

class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cell[] cells;
	private int numberOfMines;

	public Board() {
		initializeClass();
		setLayout(new GridLayout(Constants.BOARD_ROWS, Constants.BOARD_COLUMNS));
		initializeBoard();
	}

	public synchronized void incrementBombNumber() {
		this.numberOfMines++;
	}

	public synchronized void decrementBombNumber() {
		this.numberOfMines--;
	}

	private void initializeBoard() {
		for (int i = 0; i < Constants.BOARD_COLUMNS * Constants.BOARD_ROWS; i++) {

			cells[i] = new Cell(i + 1);
			add(cells[i]);

			int row = (i / Constants.BOARD_ROWS) % 2;

			if (row == 0) {
				cells[i].setBackground(i % 2 == 0 ? Color.GRAY : Color.WHITE);
			} else {
				cells[i].setBackground(i % 2 == 0 ? Color.WHITE : Color.GRAY);
			}
		}
	}

	private void initializeClass() {
		this.cells = new Cell[Constants.BOARD_ROWS * Constants.BOARD_COLUMNS];
		this.numberOfMines = 0;
	}

	public void setMine(int index){
		cells[index].lock();
		incrementBombNumber();
		cells[index].setBackground(Color.RED);
		cells[index].unlock();
		
		sleepThread(500);
	}
	
	public void sweepMine(int index){
		
		cells[index].lock();
		decrementBombNumber();
		
		int row = (index / Constants.BOARD_ROWS ) % 2;
		
		if( row == 0 ){
			cells[index].setBackground(index%2==0 ? Color.GRAY : Color.WHITE);
		}else{
			cells[index].setBackground(index%2==0 ? Color.WHITE : Color.GRAY);
		}
		
		cells[index].unlock();
		
		sleepThread(500);
		
	}
	
	public void clearBoard(){
		for (int i = 0; i < Constants.BOARD_COLUMNS * Constants.BOARD_ROWS; i++) {

			int row = (i / Constants.BOARD_ROWS) % 2;

			if (row == 0) {
				cells[i].setBackground(i % 2 == 0 ? Color.GRAY : Color.WHITE);
			} else {
				cells[i].setBackground(i % 2 == 0 ? Color.WHITE : Color.GRAY);
			}
		}
	}

	private void sleepThread(int duration) {
		try{
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public int getNumberOfMines(){
		return this.numberOfMines;
	}
}

class Cell extends JPanel{

	private static final long serialVersionUID = 1L;
	private int id;
	private Lock lock;
	private State state;
	private boolean hasBomb;
	
	public Cell(int id) {
		initVariables(id);
		setLayout(new GridLayout());
	}

	private void initVariables(int id) {
		this.id = id;
		this.lock = new ReentrantLock();
		this.state = State.EMPTY;
		this.hasBomb = false;
	}
	
	public void lock(){
		try{
			lock.tryLock(10,TimeUnit.HOURS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void unlock(){
		lock.unlock();
	}

	@Override
	public String toString() {
		return ""+this.id+"-"+state.toString()+"-"+hasBomb;
	}
}

class Toolbar extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton startButton;
	private JButton stopButton;
	private ButtonListener listener;
	
	public Toolbar(){
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		initVariables();
		
		add(startButton);
		add(stopButton);
	}

	private void initVariables() {
		this.startButton = new JButton("Start");
		this.stopButton = new JButton("Stop");
		this.startButton.addActionListener(this);
		this.stopButton.addActionListener(this);
	}

	public void setButtonListener(ButtonListener listener){
		this.listener = listener;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if( (JButton) event.getSource() == startButton && this.listener != null ){
			this.listener.startClicked();
		}else{
			this.listener.stopClicked();
		}
	}
}

interface ButtonListener {
	public void startClicked();
	public void stopClicked();
}
