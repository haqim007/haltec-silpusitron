package com.haltec.silpusitron.data.mechanism


fun <ResultType, ResultErrorType> Resource<ResultType>.handle(handler: ResourceHandler<ResultType>){
    when (this) {
        is Resource.Success -> handler.onSuccess(this.data)
        is Resource.Error -> handler.onError(this.message, this.data, this.httpCode)
        is Resource.Loading -> handler.onLoading()
        else -> handler.onIdle()
    }
    handler.onAll(this)
}

interface ResourceHandler<ResultType> {
    fun onSuccess(data: ResultType?)
    fun onError(message: String?, data: ResultType? = null, code: Int? = null){}
    fun onLoading(){}
    fun onIdle(){}
    fun onAll(resource: Resource<ResultType>){}
}
