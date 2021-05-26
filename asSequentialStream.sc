+ Array {
  asSequentialStream {|repeats=1|
    ^Pseq(this, repeats).asStream;
  }
}

+ Pattern {
  asSequentialStream {|repeats=1|
    ^Pn(this, repeats).asStream;
  }
}

+ Object {
  asSequentialStream {|repeats=1|
    ^Pn(this, repeats).asStream;
  }
}
