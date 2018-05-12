import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import simulator.About;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class CLI {
	
	private Label[] registers;
	private ArmSimulator simulator;
	private MultiWindowTextGUI gui;
	private Window window;
	private Map<Label,Label> memory;
	private TextBox textBox;
	private TextBox console;
	
    public CLI() {
    	this.registers = new Label[16];
    	this.simulator = new ArmSimulator();
    	this.memory = new HashMap<>();
    	
		try {
			Terminal terminal;
			Screen screen = null;
			terminal = new DefaultTerminalFactory().createTerminal();
	
	        screen = new TerminalScreen(terminal);
	        screen.startScreen();
	        
	        TerminalSize size = screen.getTerminalSize();
	        
	        
	        Panel masterPanel = new Panel();
	    	masterPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
	    	
	    	Panel menuPanel = new Panel();
	    	menuPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
	    	masterPanel.addComponent(menuPanel);
	    	
	    	menuPanel.addComponent(new Button("Run", () -> {
	    		this.textBox.setText("COUCOU");
	    	}));
	    	
	    	menuPanel.addComponent(new Button("Run Step", () -> {
	    		this.textBox.setText("COUCOU");
	    	}));
	    	
	    	menuPanel.addComponent(new Button("Exit", () -> {
	    		System.exit(0);
	    	}));
	    	
	    	Panel bodyPanel = new Panel();
	    	bodyPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
	    	masterPanel.addComponent(bodyPanel);
	    	
	    	Panel leftPanel = new Panel(new GridLayout(2));
	    	bodyPanel.addComponent(leftPanel.withBorder(Borders.singleLine("Registers")));
	    	IntStream.range(0, 16).forEachOrdered(n -> {
	    	    leftPanel.addComponent(new Label("r" + n));
	    	    this.registers[n] = new Label("0");
	    	    leftPanel.addComponent(this.registers[n]);
	    	});
	    	
	    	
	    	Panel centerPanel = new Panel();
	    	bodyPanel.addComponent(centerPanel);
	    	textBox = new TextBox(new TerminalSize(size.getColumns()/2,size.getColumns()/8));
	    	centerPanel.addComponent(textBox.withBorder(Borders.singleLine("Editor")));
	    	
	    	Panel rightPanel = new Panel(new GridLayout(2));
	    	bodyPanel.addComponent(rightPanel.withBorder(Borders.singleLine("Memory")));
	    	IntStream.range(0, 16).forEachOrdered(n -> {
	    		Label key = new Label("0x" + n);
	    	    Label value = new Label("0");
	    	    rightPanel.addComponent(key);
	    	    rightPanel.addComponent(value);
	    	    memory.put(key, value);
	    	});
	    	
	    	Panel bottomPanel = new Panel();
	    	bodyPanel.addComponent(bottomPanel.withBorder(Borders.singleLine()));
	    	bottomPanel.addComponent(new Button("⇡ Lo", () -> {
	    		this.textBox.setText("COUCOU");
	    	}));
	    	bottomPanel.addComponent(new Button("⇣ Hi", () -> {
	    		this.textBox.setText("COUCOU");
	    	}));
	    	this.console = new TextBox(new TerminalSize(size.getColumns()/2,4));
	    	this.console.setReadOnly(true);
	    	this.console.setText(About.info() + "\n\n\n\n\na");
	    	centerPanel.addComponent(this.console.withBorder(Borders.singleLine("Console")));
	    	
	    	window = new BasicWindow();
	        window.setComponent(masterPanel.withBorder(Borders.doubleLine("@rmSim")));
	    	window.setHints(Arrays.asList(Window.Hint.CENTERED));	
	        // Create gui and start gui
	        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	        gui.addWindowAndWait(window);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void start() {
        gui.addWindowAndWait(window);
       }
    
}
