+ TempoClock {
  bpm_ { |newVal|
    this.tempo = newVal / 60;
  }

  bpm {
    ^(this.tempo * 60);
  }

  playQuant {|quant, task|
    this.schedAbs(this.nextTimeOnGrid(quant), task);
  }
}
