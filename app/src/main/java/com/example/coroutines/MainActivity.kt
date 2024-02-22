package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis
import kotlin.time.measureTimedValue

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         GlobalScope.launch { // this: CoroutineScope
             // launch a new coroutine and continue
                delay(5000L) // non-blocking delay for 1 second (default time unit is ms)
             Log.i("ahmed", "hello from global scope at thread ${Thread.currentThread().name}") // print after delay

        }
        Log.i("ahmed", "hello from ${Thread.currentThread().name} thread") // print after delay



        Log.i("ahmed", "before runBlocking")
        runBlocking {
            Log.i("ahmed", "run blocking started")

            launch(Dispatchers.IO) {
                delay(3000L)
                Log.i("ahmed", "L1")
            }
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.i("ahmed", "L2")
            }
            delay(2000L)
            Log.i("ahmed", "R1")
            delay(1000L)
        }
        Log.i("ahmed", "M1")


        // job
        val job = GlobalScope.launch(Dispatchers.Default) {

            // withTimeout -> it will cancel after the seconds on it's param
            withTimeout(3000L) {
                repeat(4) {
                    if (isActive)
                    {
                        Log.i("ahmed", "still working ")
                        delay(1000L)
                    }
                }
            }


        }

        runBlocking {
            // join -> Suspends the coroutine until this job is complete
//            job.join()

            delay(2000L)
            job.cancel()  // cancel -> cancel the job
            Log.i("ahmed", "Done")
        }



    }

    suspend fun networkCall1(): String
    {
        delay(2000L)
        return "Answer 1"
    }

    suspend fun networkCall2(): String
    {
        delay(3000L)
        return "Answer 2"
    }

    override fun onResume()
    {
        super.onResume()

        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {

                val answer1 = async { networkCall1() }
                val answer2 = async { networkCall2() }
                Log.i("ahmed", answer2.await())
                Log.i("ahmed", answer1.await())

            }
            Log.i("ahmed", "Done in $time")
        }
    }

}
