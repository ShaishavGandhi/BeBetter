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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class CalendarExtensionsUTest {

  @Test fun testCalendarYesterday() {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1)
    val newCalendar = Calendar.getInstance()
    assertEquals(calendar.get(Calendar.DAY_OF_YEAR), newCalendar.yesterday().get(Calendar.DAY_OF_YEAR))
  }

  @Test fun testCalendarTomorrow() {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
    val newCalendar = Calendar.getInstance()
    assertEquals(calendar.get(Calendar.DAY_OF_YEAR), newCalendar.tomorrow().get(Calendar.DAY_OF_YEAR))
  }

}