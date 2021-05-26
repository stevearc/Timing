Animation {
  *lerp { |start, end, dur=1, tstep=0.05, clock=nil, callback|
    var env = Env([start, end], [dur]);
    ^Animation.animate(env, tstep, clock, callback);
  }

  *elerp { |start, end, dur=1, curve=1, tstep=0.05, clock=nil, callback|
    var env = Env([start, end], [dur], curve);
    ^Animation.animate(env, tstep, clock, callback);
  }

  *animate { |env, tstep=0.05, clock=nil, callback|
    var stream = env.asStream;
    var iterations = env.totalDuration / tstep;
    var nextVal;
    ^Task({
      iterations.do {
        callback.value(stream.next);
        tstep.wait;
      };
      nextVal = stream.next;
      callback.value(nextVal);
      // Make sure we made it to the end of the stream
      while {nextVal != env.levels[env.levels.size - 1]} {
        tstep.wait;
        nextVal = stream.next;
        callback.value(nextVal);
      }
    }).start(clock);
  }
}
