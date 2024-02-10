package com.softyorch.mvvmjetpackcompose

import com.softyorch.mvvmjetpackcompose.ui.componens.DataFieldTest
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAutoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DataFieldTest::class,
    ImageUserAutoTest::class
)
class AndroidMainUITestSuite