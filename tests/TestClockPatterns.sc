TestClockPatterns : UnitTest {

  test_PCdownbeat {
    var cases = [
      [PCdownbeat([1,2,3,4]), [1,1,1,1]],
      [PCdownbeat([1,2,3]), [1,1,2]],
      [PCdownbeat([1,Rest(2),3]), [1,Rest(1),2]],
      [PCdownbeat([Rest(2),3]), [Rest(1), Rest(1),2]],
      [PCdownbeat([2,4]), [Rest(1),2,1]],
      [PCdownbeat([2,4],2), [Rest(1),2,1,Rest(1),2,1]],
      [PCdownbeat([1.5,2,4]), [Rest(0.5),0.5,2,1]],
      [PCdownbeat([1,4,4.5],2), [3,0.5,0.5,3,0.5,0.5]],
    ];
    cases.do { |c|
      var values = c[0].asStream.all;
      this.assert(values == c[1], "Pattern " ++ c[0].list ++ "\n\tExpected: " ++ c[1] ++ "\n\tReceived: " ++ values);
    }
  }

  test_PCser {
    var cases = [
      [PCser([1,1,1,1],4), [1,1,1,1]],
      [PCser([1,1,1],4), [1,1,1,1]],
      [PCser([1,2,2],4), [1,2,1]],
      [PCser([1,2,2],4,2), [1,2,1,1,2,1]],
    ];
    cases.do { |c|
      var values = c[0].asStream.all;
      this.assert(values == c[1], "Pattern " ++ c[0] ++ "\n\tExpected: " ++ c[1] ++ "\n\tReceived: " ++ values);
    }
  }
}
