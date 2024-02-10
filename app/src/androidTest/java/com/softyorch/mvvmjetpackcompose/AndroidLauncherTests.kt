package com.softyorch.mvvmjetpackcompose

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AndroidMainTestSuite::class,
    AndroidMainUITestSuite::class
)
class AndroidLauncherTests