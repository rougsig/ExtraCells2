package extracells.feature.certustank

import cpw.mods.fml.common.Loader
import extracells.core.entity.ECFluidStack
import extracells.extension.toMap
import extracells.feature.block.ECBlock
import extracells.feature.block.certustank.CERTUS_TANK_CAPACITY
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import net.minecraft.init.Bootstrap
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagString
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidRegistry

internal class CertusTankTileEntityTest : ExpectSpec({
  context("single tank") {
    lateinit var entity: CertusTankTileEntity
    lateinit var world: World

    beforeTest {
      entity = CertusTankTileEntity()
      entity.xCoord = 0
      entity.yCoord = 1
      entity.zCoord = 0

      world = mockk<World>()
      entity.worldObj = world

      every { world.getTileEntity(0, 0, 0) } returns null
      every { world.getTileEntity(0, 1, 0) } returns entity
      every { world.getTileEntity(0, 2, 0) } returns null
    }

    context("empty storage") {
      expect("fill fluid") {
        // Arrange
        // no-op

        // Act
        val filled = entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Assert
        filled shouldBe 1000
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
      }

      expect("fill fluid coalesce with limit") {
        // Arrange
        // no-op

        // Act
        val filled = entity.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY + 4000,
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }

      expect("drain fluid") {
        // Arrange
        // no-op

        // Act
        val drained = entity.drain(
          fluidName = null,
          amount = 1000,
          doDrain = true,
        )

        // Assert
        drained shouldBe extracells.core.entity.ECFluidStack.Empty
        entity.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString("")
        )
      }

      expect("drain fluid coalesce with limit") {
        // Arrange
        // no-op

        // Act
        val filled = entity.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY + 4000,
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }
    }

    context("not empty storage") {
      expect("fill fluid if stored the same type") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Assert
        filled shouldBe 1000
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(2000),
        )
      }

      expect("not fill fluid if stored not the same type") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          fluidName = "water",
          amount = 1000,
          doFill = true,
        )

        // Assert
        filled shouldBe 0
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
      }

      expect("fill fluid coalesce with limit") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY,
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY - 1000
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }

      expect("drain fluid the same type") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val drained = entity.drain(
          fluidName = "lava",
          amount = 1000,
          doDrain = true,
        )

        // Assert
        drained shouldBe ECFluidStack("lava", 1000)
        entity.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }

      expect("drain fluid not the same type") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val drained = entity.drain(
          fluidName = "water",
          amount = 1000,
          doDrain = true,
        )

        // Assert
        drained shouldBe ECFluidStack.Empty
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
      }

      expect("drain fluid coalesce with limit") {
        // Arrange
        entity.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Act
        val drained = entity.drain(
          fluidName = "lava",
          amount = 2000,
          doDrain = true,
        )

        // Assert
        drained shouldBe extracells.core.entity.ECFluidStack("lava", 1000)
        entity.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }
    }
  }

  context("tank tower") {
    lateinit var entity1: CertusTankTileEntity
    lateinit var entity2: CertusTankTileEntity
    lateinit var entity3: CertusTankTileEntity
    lateinit var entity4: CertusTankTileEntity
    lateinit var world: World

    beforeTest {
      entity1 = CertusTankTileEntity()
      entity1.xCoord = 0
      entity1.yCoord = 1
      entity1.zCoord = 0

      entity2 = CertusTankTileEntity()
      entity2.xCoord = 0
      entity2.yCoord = 2
      entity2.zCoord = 0

      entity3 = CertusTankTileEntity()
      entity3.xCoord = 0
      entity3.yCoord = 3
      entity3.zCoord = 0

      entity4 = CertusTankTileEntity()
      entity4.xCoord = 0
      entity4.yCoord = 4
      entity4.zCoord = 0

      world = mockk<World>()
      entity1.worldObj = world
      entity2.worldObj = world
      entity3.worldObj = world
      entity4.worldObj = world

      every { world.getTileEntity(0, 0, 0) } returns null
      every { world.getTileEntity(0, 1, 0) } returns entity1
      every { world.getTileEntity(0, 2, 0) } returns entity2
      every { world.getTileEntity(0, 3, 0) } returns entity3
      every { world.getTileEntity(0, 4, 0) } returns entity4
      every { world.getTileEntity(0, 5, 0) } returns null
    }

    context("empty tower of 4 tanks") {
      expect("valid tank info") {
        // Arrange
        // no-op

        // Act
        val info = entity3.getTankInfo(ForgeDirection.UNKNOWN)

        // Assert
        info.size shouldBe 1
        info[0].fluid shouldBe null
        info[0].capacity shouldBe CERTUS_TANK_CAPACITY * 4
      }

      expect("fill 3 stored in 1") {
        // Arrange
        // no-op

        // Act
        val filled = entity3.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )

        // Assert
        filled shouldBe 1000
        entity1.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
        entity2.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity3.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity4.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }

      expect("fill lava and water in 3 stored lave in 1 water in 2") {
        // Arrange
        // no-op

        // Act
        val filledLava = entity3.fill(
          fluidName = "lava",
          amount = 1000,
          doFill = true,
        )
        val filledWater = entity3.fill(
          fluidName = "water",
          amount = 1000,
          doFill = true,
        )

        // Assert
        filledLava shouldBe 1000
        filledWater shouldBe 1000
        entity1.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
        entity2.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("water"),
          "Amount" to NBTTagInt(1000),
        )
        entity3.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity4.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }

      expect("fill 4 tank in one operation") {
        // Arrange
        // no-op

        // Act
        val filled = entity3.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY * 4,
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY * 4
        entity1.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity2.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity3.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity4.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }

      expect("fill tower coalesce with limit") {
        // Arrange
        // no-op

        // Act
        val filled = entity3.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY * 5,
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY * 4
        entity1.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity2.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity3.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
        entity4.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }
    }

    context("not empty tower of 4 tanks") {
      expect("valid tank info") {
        // Arrange
        entity4.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY + CERTUS_TANK_CAPACITY / 2,
          doFill = true,
        )
        entity4.fill(
          fluidName = "water",
          amount = CERTUS_TANK_CAPACITY + CERTUS_TANK_CAPACITY / 2,
          doFill = true,
        )

        // Act
        val info1 = entity1.getTankInfo(ForgeDirection.UNKNOWN)
        val info3 = entity3.getTankInfo(ForgeDirection.UNKNOWN)

        // Assert
        info1[0].fluid.getFluid() shouldBe FluidRegistry.LAVA
        info1[0].fluid.amount shouldBe CERTUS_TANK_CAPACITY + CERTUS_TANK_CAPACITY / 2
        info1[0].capacity shouldBe CERTUS_TANK_CAPACITY * 2

        info3[0].fluid.getFluid() shouldBe FluidRegistry.WATER
        info3[0].fluid.amount shouldBe CERTUS_TANK_CAPACITY + CERTUS_TANK_CAPACITY / 2
        info3[0].capacity shouldBe CERTUS_TANK_CAPACITY * 2
      }

      expect("drain fluid coalesce with limit") {
        // Arrange
        entity3.fill(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY * 3,
          doFill = true,
        )

        // Act
        val drained = entity1.drain(
          fluidName = "lava",
          amount = CERTUS_TANK_CAPACITY * 4,
          doDrain = true,
        )

        // Assert
        drained shouldBe ECFluidStack("lava", CERTUS_TANK_CAPACITY * 3)
        entity1.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity2.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity3.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
        entity4.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }
    }
  }
}) {
  init {
    // Load minecraft
    mockkStatic(Loader::instance)
    every { Loader.instance() } returns mockk() {
      every { activeModContainer() } returns mockk() {
        every { modId } returns "minecraft"
      }
    }
    Bootstrap.func_151354_b()

    TileEntity.addMapping(CertusTankTileEntity::class.java, ECBlock.CertusTank.block.internalName)
  }
}

private val CertusTankTileEntity.nbt: Map<String, Any>
  get() = NBTTagCompound().apply(this::writeToNBT).toMap()
