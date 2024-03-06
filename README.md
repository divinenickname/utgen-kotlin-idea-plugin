[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# UTGen Kotlin IDEA IDE plugin

Simple tool for generating unit-test for Kotlin code. 
Based on [UTGen library](https://github.com/divinenickname/utgen-kotlin-core).

## Features
- Supported language: Kotlin.
- Generate stub unit-tests.

## How to use
1. Find your code.
2. Right click on kotlin file.
3. Choose "Generate Unit-Tests".
4. You can find test in test folder.

![Screenshot 1](./images/1.png)
![Screenshot 2](./images/2.png)
![Screenshot 3](./images/3.png)

### Example

Your source class is:
```kotlin
package io.github.divinenickname.kotlin.utgen.core

class TestClass {

    fun voidMethod() {
        println("VOID method")
    }

    fun nonVoidMethod(): String {
        return "abcd"
    }

    public fun publicScopeMethod() {}

    private fun privateMethod(int: Long): String {
        return "private scope method"
    }
}
```

Library generates code:
```kotlin
package io.github.divinenickname.kotlin.utgen.core

import org.junit.jupiter.api.Test

internal class TestClassTest {
  private val obj: TestClassTest = TestClass()

  @Test
  public fun voidMethod_goldencase() {
    TODO("Implement")
    val actual = obj.voidMethod()
  }

  @Test
  public fun nonVoidMethod_goldencase() {
    TODO("Implement")
    val actual = obj.nonVoidMethod()
  }

  @Test
  public fun publicScopeMethod_goldencase() {
    TODO("Implement")
    val actual = obj.publicScopeMethod()
  }
}
```
