+ Synth {
  lerp { |key, start=nil, end, dur=1, tstep=0.05, clock=nil|
    if (start.isNil) {
      this.get(key, this.lerp(key, _, end, dur, tstep, clock));
    } {
      ^Animation.lerp(start, end, dur, tstep, clock, this.set(key, _));
    };
  }

  elerp { |key, start=nil, end, dur=1, curve=1, tstep=0.05, clock=nil|
    if (start.isNil) {
      this.get(key, this.elerp(key, _, end, dur, curve, tstep, clock));
    } {
      ^Animation.elerp(start, end, dur, curve, tstep, clock, this.set(key, _));
    };
  }

  animate { |key, env, tstep=0.05, clock=nil|
    if (env.levels[0].isNil) {
      this.get(key, {|start|
        env.levels[0] = start;
        this.animate(key, env, tstep, clock);
      });
    } {
      ^Animation.animate(env, tstep, clock, this.set(key, _));
    };
  }
}

+ NodeProxy {
  lerp { |key, start, end, dur=1, tstep=0.05, clock=nil|
    ^Animation.lerp(start, end, dur, tstep, clock, this.set(key, _));
  }

  elerp { |key, start, end, dur=1, curve=1, tstep=0.05, clock=nil|
    ^Animation.lerp(start, end, dur, curve, tstep, clock, this.set(key, _));
  }

  animate { |key, env, tstep=0.05, clock=nil|
    ^Animation.animate(env, tstep, clock, this.set(key, _));
  }
}
