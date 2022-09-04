package com.bolyartech.forge.test.dagger

import com.bolyartech.forge.server.db.DbConfiguration
import dagger.Module

@Module
class DbDaggerModule(private val dbConfig: DbConfiguration) {

}

@Module
abstract class DbDaggerModuleBinds {

}