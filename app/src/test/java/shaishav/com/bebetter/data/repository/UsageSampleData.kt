package shaishav.com.bebetter.data.repository

import shaishav.com.bebetter.data.models.Usage

/**
 * Created by shaishav.gandhi on 12/25/17.
 */
object UsageSampleData {

    fun getSampleUsages(count: Int): List<Usage> {
        val usages = ArrayList<Usage>()
        for (i in 0..count) {
            val usage = Usage()
            usages.add(usage)
        }
        return usages
    }

}