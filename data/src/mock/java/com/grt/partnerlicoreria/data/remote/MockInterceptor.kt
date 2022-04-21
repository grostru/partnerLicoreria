package com.grt.pokemon.data.remote

import android.content.Context
import com.grt.pokemon.data.R
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MockInterceptor(private val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        generateRandomException()

        val randomResponse = if((0..1).random() == 0)
            200
        else
            500

        val response =
            Response.Builder().protocol(Protocol.HTTP_1_0).code(randomResponse).request(chain.request())
        addResponse(response)

        return response.build()
    }

    private fun addResponse(response: Response.Builder) {
        val mockJson = getMockJSONResource(R.raw.pokemons,context)
        response.message(mockJson)
        response.body(ResponseBody.create(MediaType.get("application/json"),mockJson))
    }

    private fun getMockJSONResource(resource: Int, context: Context): String {
        val inputStream = context.resources.openRawResource(resource)
        val br = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?

        line = br.readLine()

        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }
        br.close()
        return sb.toString()
    }


    fun generateRandomException(){
        val number = (0..1).random()
        if(number == 0){
            throw IOException()
        }
    }
}