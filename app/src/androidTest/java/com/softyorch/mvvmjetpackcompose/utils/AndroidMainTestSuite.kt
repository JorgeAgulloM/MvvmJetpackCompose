package com.softyorch.mvvmjetpackcompose.utils

import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionDialTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionEmail
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionEmailTest
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionSMSTest
import com.softyorch.mvvmjetpackcompose.data.entity.UserDaoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    UserDaoTest::class,
    ActionDialTest::class,
    ActionEmailTest::class,
    ActionSMSTest::class
)
class AndroidMainTestSuite