Animation {
  *lerp { |start, end, dur=1, tstep=0.05, callback, clock|
    var env = Env([start, end], [dur]);
    ^Animation.animate(env, tstep, callback, clock);
  }

  *elerp { |start, end, dur=1, curve=1, tstep=0.05, callback, clock|
    var env = Env([start, end], [dur], curve);
    ^Animation.animate(env, tstep, callback, clock);
  }

  *animate { |env, tstep=0.05, callback, clock|
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

+ Synth {
  lerp { |key, start=nil, end, dur=1, tstep=0.05, clock|
    if (start.isNil) {
      this.get(key, this.lerp(key, _, end, dur, tstep, clock));
    } {
      ^Animation.lerp(start, end, dur, tstep, this.set(key, _), clock);
    };
  }

  elerp { |key, start=nil, end, dur=1, curve=1, tstep=0.05, clock|
    if (start.isNil) {
      this.get(key, this.elerp(key, _, end, dur, curve, tstep, clock));
    } {
      ^Animation.elerp(start, end, dur, curve, tstep, this.set(key, _), clock);
    };
  }

  animate { |key, env, tstep=0.05, clock|
    if (env.levels[0].isNil) {
      this.get(key, {|start|
        env.levels[0] = start;
        this.animate(key, env, tstep, clock);
      });
    } {
      ^Animation.animate(env, tstep, this.set(key, _), clock);
    };
  }
}

+ NodeProxy {
  lerp { |key, start, end, dur=1, tstep=0.05, clock|
    ^Animation.lerp(start, end, dur, tstep, this.set(key, _), clock);
  }

  elerp { |key, start, end, dur=1, curve=1, tstep=0.05, clock|
    ^Animation.lerp(start, end, dur, curve, tstep, this.set(key, _), clock);
  }

  animate { |key, env, tstep=0.05, clock|
    ^Animation.animate(env, tstep, this.set(key, _), clock);
  }
}
