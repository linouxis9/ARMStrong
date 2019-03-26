/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.io;

public class IO8Segment {

	private final IOSegment[] segments;
	
	protected IO8Segment(IOSegment[] segments) {
		this.segments = segments;
	}

	public boolean getSegmentState(int noSegment) {
		return segments[noSegment].isOn();
	}
	
	public IOSegment getSegment(int noSegment) {
		return segments[noSegment];
	}
	
	public void setSegment(int noSegment, boolean value) {
		this.getSegment(noSegment).set(value);;
	}
}
