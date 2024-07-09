package com.haltec.silpusitron.data.mechanism

import com.haltec.silpusitron.data.util.CustomRequestException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.ExpectationFailed
import kotlinx.serialization.json.JsonElement

suspend fun <T> getResult(callback: suspend () -> T): Result<T> {
    return try {
        Result.success(callback())
    }
    catch (e: ConnectTimeoutException) {

        Result.failure(
            CustomThrowable(
                code = ExpectationFailed,
                message = e.localizedMessage
            )
        )

    }
    catch (e: HttpRequestTimeoutException) {
        Result.failure(
            CustomThrowable(
                code = ExpectationFailed,
                message = "Request timeout has expired!"
            )
        )
    }
    catch (e: ClientRequestException) {

        Result.failure(
            CustomThrowable(
                code = e.response.status,
                message = e.localizedMessage
            )
        )

    }catch (e: ResponseException) {

        Result.failure(
            CustomThrowable(
                code = e.response.status,
                message = e.message
            )
        )

    } catch (e: CustomRequestException) {
        Result.failure(
            CustomThrowable(
                code = e.statusCode,
                message = e.errorMessage,
                dataJson = e.dataJson
            )
        )
    } catch (e: Exception) {
        Result.failure(
            CustomThrowable(
                code = ExpectationFailed,
                message = e.message
            )
        )
    }
}


class CustomThrowable(
    val code: HttpStatusCode = HttpStatusCode.OK,
    val dataJson: JsonElement? = null,
    override val message: String?
) : Throwable(message)