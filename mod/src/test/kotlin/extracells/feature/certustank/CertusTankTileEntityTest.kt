package extracells.feature.certustank

import cpw.mods.fml.common.Loader
import extracells.extension.toMap
import extracells.feature.ECBlock
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
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
import net.minecraftforge.fluids.FluidStack

class CertusTankTileEntityTest : ExpectSpec({
  context("single tank") {
    context("empty storage") {
      expect("fill fluid") {
        // Arrange
        val entity = CertusTankTileEntity()

        // Act
        val filled = entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
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
        val entity = CertusTankTileEntity()

        // Act
        val filled = entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, CERTUS_TANK_CAPACITY + 4000),
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
        val entity = CertusTankTileEntity()

        // Act
        val drained = entity.drain(
          from = ForgeDirection.UNKNOWN,
          maxDrain = 1000,
          doDrain = true,
        )

        // Assert
        drained shouldBe null
        entity.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString("")
        )
      }
    }

    context("not empty storage") {
      expect("fill fluid if stored the same type") {
        // Arrange
        val entity = CertusTankTileEntity()
        entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
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
        val entity = CertusTankTileEntity()
        entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.WATER, 1000),
          doFill = true,
        )

        // Assert
        filled shouldBe 0
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
      }

      expect("drain fluid the same type") {
        // Arrange
        val entity = CertusTankTileEntity()
        entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doFill = true,
        )

        // Act
        val drained = entity.drain(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doDrain = true,
        )

        // Assert
        drained shouldBe FluidStack(FluidRegistry.LAVA, 1000)
        entity.nbt shouldContainAll mapOf(
          "Empty" to NBTTagString(""),
        )
      }

      expect("drain fluid not the same type") {
        // Arrange
        val entity = CertusTankTileEntity()
        entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doFill = true,
        )

        // Act
        val drained = entity.drain(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.WATER, 1000),
          doDrain = true,
        )

        // Assert
        drained shouldBe null
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(1000),
        )
      }

      expect("fill fluid coalesce with limit") {
        // Arrange
        val entity = CertusTankTileEntity()
        entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, 1000),
          doFill = true,
        )

        // Act
        val filled = entity.fill(
          from = ForgeDirection.UNKNOWN,
          resource = FluidStack(FluidRegistry.LAVA, CERTUS_TANK_CAPACITY),
          doFill = true,
        )

        // Assert
        filled shouldBe CERTUS_TANK_CAPACITY - 1000
        entity.nbt shouldContainAll mapOf(
          "FluidName" to NBTTagString("lava"),
          "Amount" to NBTTagInt(CERTUS_TANK_CAPACITY),
        )
      }
    }
  }

  context("tank tower") {
    context("empty tower of 4 tanks") {
      expect("valid tank info") {
        // Arrange
        val entity1 = CertusTankTileEntity()
        entity1.xCoord = 0
        entity1.yCoord = 1
        entity1.zCoord = 0
        val entity2 = CertusTankTileEntity()
        entity2.xCoord = 0
        entity2.yCoord = 2
        entity2.zCoord = 0
        val entity3 = CertusTankTileEntity()
        entity3.xCoord = 0
        entity3.yCoord = 3
        entity3.zCoord = 0
        val entity4 = CertusTankTileEntity()
        entity4.xCoord = 0
        entity4.yCoord = 4
        entity4.zCoord = 0

        val world = mockk<World>()
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

        // Act
        val info = entity3.getTankInfo(ForgeDirection.UNKNOWN)

        // Assert
        info.size shouldBe 1
        info[0].fluid shouldBe null
        info[0].capacity shouldBe CERTUS_TANK_CAPACITY * 4
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