package example.com.mydiary.utils

import kotlinx.coroutines.experimental.Job

/**
 * Created by saikr on 30-03-2018.
 */
interface JobHolder {
    val job: Job
}