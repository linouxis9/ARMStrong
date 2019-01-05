package projetarm_v2.simulator.ui.javafx;

public class RamTuple {
    private int address;
    private String content;
    private int mode;

    public RamTuple(int address, int mode){
        this.address = address;
        switch (mode){
            case 8:
                this.content = "008"; //get content from simulator
                break;
            case 16:
                this.content = "0016";
                break;
            case 32:
                this.content = "0032";
                break;
        }
    }

    public int getAddress(){
        return address;
    }
    public String getContent(){
        return content;
    }
}
