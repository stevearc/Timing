PCstep : Pattern {
	var <>sequence, <>timing;
	*new { |sequence, timing|
		^super.newCopyArgs(sequence, timing)
	}
	storeArgs { ^[sequence, timing] }

	prGetStepStream { |input|
		if (input.isArray) {
			^Routine{
				var idx = 0;
				loop {
					input.wrapAt(idx).yield;
					idx = idx + 1;
				}
			};
		} {
			^input.asSequentialStream(inf);
		};
	}

	embedInStream { | inval |
		var accum = 0;
		var timingStream = timing.asSequentialStream(inf);
		var dur = timingStream.next;
		var sequenceStream = this.prGetStepStream(sequence);
		var value = sequenceStream.next;
		while { value.notNil } {
			var valueStream = nil;
			accum = accum + dur;
			while { accum > thisThread.clock.beats } {
				if (valueStream.isNil) {
					valueStream = value.asSequentialStream(inf);
				};
				valueStream.next.yield;
			};
			dur = timingStream.next;
			value = sequenceStream.next;
		}
	}
}

PCLstep : Pattern {
	var <>sequence, <>timing;
	*new { |sequence, timing|
		^super.newCopyArgs(sequence, timing)
	}
	storeArgs { ^[sequence, timing] }
	embedInStream { | inval |
		var timingStream = timing.asSequentialStream(inf);
		var valueStream = sequence.asSequentialStream(inf);
		var start = 0;
		var end = timingStream.next;
		var v1 = valueStream.next;
		var v2 = valueStream.next;
		while { v2.notNil } {
			while { end > thisThread.clock.beats } {
				(v1 + ((v2-v1)*((thisThread.clock.beats-start)/(end-start)))).yield;
			};
			v1 = v2;
			v2 = valueStream.next;
			start = end;
			end = start + timingStream.next;
		}
	}
}

PCdownbeat : ListPattern {
	var <>beats;
	*new { |timings, repeats=1, beats=nil|
		^super.new(timings, repeats).beats_(beats)
	}
	storeArgs { ^[ list, repeats, beats ] }

	embedInStream { |inval|
		var idx = 0;
		var start = 1;
		var next;
		var delta;
		while { idx < repeats } {
			var timeStream = list.asSequentialStream;
			next = timeStream.next;
			// If the first value *isn't* on the 1 downbeat, start off with a Rest
			if (next > 1) {
				Rest(next-start).yield;
				start = next;
				next = timeStream.next;
			} {
				start = next;
				next = timeStream.next;
			};

			while { next.notNil } {
				delta = next - start;
				if (start.isRest) {
					delta.yield;
				} {
					delta.value.yield;
				};
				start = next;
				next = timeStream.next;
			};
			if (beats.isNil) {
				beats = (start-1).roundUp(4);
			};
			delta = (beats+1) - start;
			if (delta > 0) {
				delta.yield;
			};

			idx = idx + 1;
			start = 1;
		};
	}
}

PCser : Pattern {
	var <>sequence, <>beats, <>repeats;
	*new { |sequence, beats, repeats=1|
		^super.newCopyArgs(sequence, beats, repeats)
	}
	storeArgs { ^[sequence, beats, repeats] }

	embedInStream { |inval|
		var idx = 0;
		var valueStream = sequence.asSequentialStream(inf);
		while { idx < repeats } {
			var dur = valueStream.next;
			var accum = dur;
			while { accum < beats } {
				dur.yield;
				dur = valueStream.next;
				accum = accum + dur;
			};
			dur = beats - (accum - dur);
			if (dur > 0) {
				dur.yield;
			};
			idx = idx + 1;
		}
	}
}
