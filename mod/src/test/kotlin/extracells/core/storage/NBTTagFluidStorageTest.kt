package extracells.core.storage

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import net.minecraft.nbt.NBTTagCompound

class NBTTagFluidStorageTest : ExpectSpec({
  context("empty storage") {
    expect("inject fluid") {
      // Arrange
      val storage = createNBTTagFluidStorage()

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,},}"
    }

    expect("inject different types of fluid separately") {
      // Arrange
      val storage = createNBTTagFluidStorage()

      // Act
      val notInjectedLavaAmount = storage.inject("lava", 1000)
      val notInjectedWaterAmount = storage.inject("water", 1000)

      // Assert
      notInjectedLavaAmount shouldBeExactly 0
      notInjectedWaterAmount shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,water:1000,},}"
    }

    expect("extract fluid") {
      // Arrange
      val storage = createNBTTagFluidStorage()

      // Act
      val extractedAmount = storage.extract("lava", 1000)

      // Assert
      extractedAmount shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }
  }

  context("not empty storage") {
    expect("inject fluid should add amount to stored fluid") {
      // Arrange
      val storage = createNBTTagFluidStorage()
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:2000,},}"
    }

    expect("extract fluid should remove amount from stored fluid") {
      // Arrange
      val storage = createNBTTagFluidStorage()
      storage.inject("lava", 2000)

      // Act
      val extractedAmount = storage.extract("lava", 1000)

      // Assert
      extractedAmount shouldBeExactly 1000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,},}"
    }

    expect("not inject fluid if max types exceeded") {
      // Arrange
      val storage = createNBTTagFluidStorage(maxTypes = 2)
      storage.inject("fluid1", 1000)
      storage.inject("fluid2", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 1000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{fluid1:1000,fluid2:1000,},}"
    }

    expect("extract fluid should remove empty fluid type") {
      // Arrange
      val storage = createNBTTagFluidStorage()
      storage.inject("lava", 2000)

      // Act
      val extractedAmount = storage.extract("lava", 2000)

      // Assert
      extractedAmount shouldBeExactly 2000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }

    expect("inject according size limit") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 1600)
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 400
      storage.freeSpace shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1600,},}"
    }

    expect("extract according stored limit") {
      // Arrange
      val storage = createNBTTagFluidStorage()
      storage.inject("lava", 1600)

      // Act
      val extractedAmount = storage.extract("lava", 2000)

      // Assert
      extractedAmount shouldBeExactly 1600
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }
  }

  context("storage size") {
    expect("calculate injected amount") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 1000, typeSize = 0)
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 1000
      storage.freeSpace shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,},}"
    }

    expect("calculate extracted amount") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 1000, typeSize = 0)
      storage.inject("lava", 1000)

      // Act
      val extractedAmount = storage.extract("lava", 1000)

      // Assert
      extractedAmount shouldBeExactly 1000
      storage.freeSpace shouldBeExactly 1000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }

    expect("calculate injected types") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 2000, typeSize = 1000)
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 1000
      storage.freeSpace shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,},}"
    }

    expect("calculate extracted types") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 2000, typeSize = 1000)
      storage.inject("lava", 1000)

      // Act
      val extractedAmount = storage.extract("lava", 1000)

      // Assert
      extractedAmount shouldBeExactly 1000
      storage.freeSpace shouldBeExactly 2000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }

    expect("not calculate already injected types") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 3000, typeSize = 1000)
      storage.inject("lava", 1000)
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.inject("lava", 1000)

      // Assert
      notInjectedAmount shouldBeExactly 1000
      storage.freeSpace shouldBeExactly 0
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:2000,},}"
    }

    expect("not calculate already extracted types") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 2000, typeSize = 1000)
      storage.inject("lava", 1000)

      // Act
      storage.extract("lava", 1000)
      storage.extract("lava", 1000)

      // Assert
      storage.freeSpace shouldBeExactly 2000
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }
  }

  context("simulation") {
    expect("inject not save in nbt") {
      // Arrange
      val storage = createNBTTagFluidStorage()

      // Act
      storage.simulateInject("lava", 1000)

      // Assert
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{},}"
    }

    expect("extract not save in nbt") {
      // Arrange
      val storage = createNBTTagFluidStorage()
      storage.inject("lava", 1000)

      // Act
      storage.simulateExtract("lava", 1000)

      // Assert
      storage.nbtTag.toString() shouldBe "{storageVersion:1,fluids:{lava:1000,},}"
    }

    expect("inject not change freeSpace") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 1024)

      // Act
      storage.simulateInject("lava", 1000)

      // Assert
      storage.freeSpace shouldBeExactly 1024
    }

    expect("extract not change freeSpace") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 1000)
      storage.inject("lava", 1000)

      // Act
      storage.simulateExtract("lava", 1000)

      // Assert
      storage.freeSpace shouldBeExactly 0
    }

    expect("inject works as real") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 2024, typeSize = 1000)
      storage.inject("lava", 1000)

      // Act
      val notInjectedAmount = storage.simulateInject("lava", 1000)

      // Assert
      storage.freeSpace shouldBeExactly 24
      notInjectedAmount shouldBeExactly 976
    }

    expect("extract works as real") {
      // Arrange
      val storage = createNBTTagFluidStorage(size = 2024)
      storage.inject("lava", 2024)

      // Act
      val extractedAmount = storage.simulateExtract("lava", 3000)

      // Assert
      storage.freeSpace shouldBeExactly 0
      extractedAmount shouldBeExactly 2024
    }
  }
})

private fun createNBTTagFluidStorage(
  storage: NBTTagCompound? = null,
  maxTypes: Int = 5,
  typeSize: Int = 0,
  size: Int = 1024 * 16,
) = NBTTagFluidStorage(
  storage = storage ?: NBTTagCompound(),
  maxTypes = maxTypes,
  typeSize = typeSize,
  size = size,
)
