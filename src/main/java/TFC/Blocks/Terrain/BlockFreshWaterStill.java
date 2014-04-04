package TFC.Blocks.Terrain;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import TFC.TerraFirmaCraft;
import TFC.Blocks.Vanilla.BlockCustomStationary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFreshWaterStill extends BlockCustomStationary
{
	public BlockFreshWaterStill()
	{
		super(Material.water);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
	 * when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		if (this.blockMaterial != Material.water)
			return 16777215;
		else
			return TerraFirmaCraft.proxy.waterColorMultiplier(par1IBlockAccess, par2, par3, par4);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block block, int l)
	{
		if(block == Blocks.ice){
			//world.setBlockMetadataWithNotify(i,j,k,1,1);
		}
		super.breakBlock(world, i, j, k, block, l);
	}

	@Override
	protected void setFreezeBlock(World world, int i, int j, int k, Random rand){
		if(world.getBlock(i,j,k).getMaterial() == Material.water)
			world.setBlock(i, j, k, Blocks.ice, 1, 1);
	}
}