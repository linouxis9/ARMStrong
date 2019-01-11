package projetarm_v2.simulator.core.io;

public class IO7Segment {

	private final IOSegment[] segments;
	
	protected IO7Segment(IOSegment[] segments) {
		this.segments = segments;
	}

	public boolean getSegmentState(int noSegment) {
		return segments[noSegment].isOn();
	}
	
	public IOSegment getSegment(int noSegment) {
		return segments[noSegment];
	}
}
