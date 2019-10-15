package ru.vladislav_akulinin.mychat_version_2.test

class MainActivityModel: ContractInterface.Model {

    private var mCounter = 0

    override fun getCounter()= mCounter

    override fun incrementCounter() {
        mCounter++
    }
}