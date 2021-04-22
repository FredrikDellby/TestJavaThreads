package MinerProject;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();
			}
		});
	}
}

class MainFrame extends JFrame implements ButtonListener {

	private static final long serialVersionUID = 1L;
	private Toolbar toolbar;
	private Board board;
	private ExecutorService layersExecutor;
	private ExecutorService sweepersExecutor;
	private MineLayer[] mineLayers;
	private MineSweeper[] mineSweepers;
	
	public MainFrame(){
		super(Constants.APP_NAME);
		
		toolbar = new Toolbar();
		board = new Board();
		
		initializeVariables();
				
		toolbar.setButtonListener(this);
		
		add(toolbar, BorderLayout.NORTH);
		add(board, BorderLayout.CENTER);
		
		setSize(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initializeVariables() {
		mineLayers = new MineLayer[Constants.NUMBER_OF_LAYERS];
		mineSweepers = new MineSweeper[Constants.NUMBER_OF_SWEEPERS];
	}

	@Override
	public void startClicked() {
		
		this.layersExecutor = Executors.newFixedThreadPool(Constants.NUMBER_OF_LAYERS);
		this.sweepersExecutor = Executors.newFixedThreadPool(Constants.NUMBER_OF_SWEEPERS);
	
		try{
			
			for(int i=0;i<Constants.NUMBER_OF_LAYERS;i++){
				mineLayers[i] = new MineLayer(i, board);
				layersExecutor.execute(mineLayers[i]);
			}
			
			for(int i=0;i<Constants.NUMBER_OF_SWEEPERS;i++){
				mineSweepers[i] = new MineSweeper(i, board);
				sweepersExecutor.execute(mineSweepers[i]);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			layersExecutor.shutdown();
			sweepersExecutor.shutdown();
		}		
	}

	@Override
	public void stopClicked() {
		
		for(MineLayer mineLayer : this.mineLayers){
			mineLayer.setLayerRunning(false);
		}
		
		for(MineSweeper mineSweeper : this.mineSweepers){
			mineSweeper.setSweeperRunning(false);
		}
		
		layersExecutor.shutdown();
		sweepersExecutor.shutdown();
		
		try {
			layersExecutor.awaitTermination(1, TimeUnit.MINUTES);
			sweepersExecutor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		layersExecutor.shutdownNow();
		sweepersExecutor.shutdownNow();
		
		this.board.clearBoard();
		
	}
}

class Constants {

	private Constants(){
		
	}
	
	public static final int NUMBER_OF_SWEEPERS = 100;
	public static final int NUMBER_OF_LAYERS = 2000;
	public static final int BOARD_ROWS = 10;
	public static final int BOARD_COLUMNS = 10;
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 650;
	public static final String APP_NAME = "Mine simulation!";
	
}

enum State {
	EMPTY, MINE, MINESWEEPER, MINELAYER;
}