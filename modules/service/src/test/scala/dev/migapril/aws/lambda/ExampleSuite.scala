package dev.migapril
package aws
package lambda

final class ExampleSuite extends TestSuite {
  test("hello world") {
    forAll { (int: Int, string: String) =>
      expect(int === int, string === string)
    }
  }
}
