+ TempoClock {
  *bpm_ { |bpm| TempoClock.default.bpm_(bpm) }
  bpm_ { |bpm|
    this.tempo = bpm / 60;
  }

  *bpm { ^TempoClock.default.bpm }
  bpm {
    ^(this.tempo * 60);
  }

  *playQuant { |quant, action| ^TempoClock.default.playQuant(quant, action) }
  playQuant { |quant, action|
    if (quant.isSequenceableCollection.not) {
      quant = [quant];
    };
    this.schedAbs(this.nextTimeOnGrid(*quant), action);
  }
}
