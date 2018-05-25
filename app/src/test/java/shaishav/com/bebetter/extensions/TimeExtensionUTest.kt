/*
 * Copyright (c) 2018 Shaishav Gandhi
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */

package shaishav.com.bebetter.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeExtensionUTest {

  @Test fun testFormatTimeBelowHour() {
    val time = 1000 * 60 * 17L
    assertEquals("17m", time.toFormattedTime())
  }

  @Test fun testFormatTimeWithZero() {
    val time = 965 * 60 * 0L
    assertEquals("0m", time.toFormattedTime())
  }

  @Test fun testFormatTimeAboveHour() {
    val time = 1000 * 60 * 235L
    assertEquals("3hr 55m", time.toFormattedTime())
  }

  @Test fun testFormatTimeExactHour() {
    val time = 1000 * 60 * 240L
    assertEquals("4hr", time.toFormattedTime())
  }

  @Test fun testFormatTimeDays() {
    val time = 1000 * 60 * 60 * 24 * 6L
    assertEquals("6 days", time.toFormattedTime())
  }

  @Test fun testFormatTimeWeeks() {
    val time = 1000 * 60 * 60 * 24 * 43L
    assertEquals("6 weeks", time.toFormattedTime())
  }
}