+ TempoClock {
  *playSeq { |quant ... pairs| ^TempoClock.default.playSeq(quant, *pairs) }
  playSeq { |quant ... pairs|
    var minPhase = inf;
    var bar = inf;
    if (pairs.size.odd, { Error("TempoClock.playSeq should have odd number of args.\n").throw; });
    forBy (0, pairs.size - 1, 2) { |i|
      var phase = pairs[i];
      var action = pairs[i+1];
      if (phase < minPhase) {
        minPhase = phase;
      }
    };
    bar = this.nextTimeOnGrid(quant, minPhase);
    forBy (0, pairs.size - 1, 2) { |i|
      var phase = pairs[i];
      var action = pairs[i+1];
      this.schedAbs(bar + (phase - minPhase), action);
    };
  }
}
