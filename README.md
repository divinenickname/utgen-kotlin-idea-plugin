<img width="100px" src="./logo.svg"  alt="Logo image. Red circle with letter U inside."/><br>

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
package com.example.demo1

class MyTestClass(
    private val a: String,
    private val b: String,
) {

    fun testMethod(): MyRetObj {
        return MyRetObj(true)
    }
}
```
```kotlin
data class MyRetObj(
    val a: Boolean
)
```

Library generates code:
```kotlin
package com.example.demo1

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MyTestClassTest {
    private val obj: MyTestClass = MyTestClass()

    @Test
    public fun testMethod_goldencase() {
        TODO("Implement")
        val expected = MyRetObj()
        val actual = obj.testMethod()

        Assertions.assertEquals(expected, actual)
    }
}
```
