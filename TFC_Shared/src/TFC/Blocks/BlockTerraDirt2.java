package TFC.Blocks;

import java.util.ArrayList;
import java.util.Random;

import TFC.Entities.EntityFallingDirt;

import net.minecraft.src.*;

public class BlockTerraDirt2 extends BlockTerra2
{	
    public BlockTerraDirt2(int i, int j, Block Farm)
    {
        super(i, j, Material.ground);
    }

    public void addCreativeItems(java.util.ArrayList list)
    {
        for(int i = 0; i < 7; i++) {
            list.add(new ItemStack(this,1,i));
        }
    }

    @Override
    protected int damageDropped(int i) {
        return i;
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return this.blockIndexInTexture + par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return this.blockIndexInTexture + par2;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
    }
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
    }

    public int tickRate()
    {
        return 3;
    }

    private void tryToFall(World world, int i, int j, int k)
    {
        if(!world.isRemote)
        {
            int meta = world.getBlockMetadata(i, j, k);
            if (!BlockCollapsable.isNearSupport(world, i, j, k) && canFallBelow(world, i, j - 1, k) && j >= 0)
            {
                byte byte0 = 32;
                if (!world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0))
                {
                    world.setBlockWithNotify(i, j, k, 0);
                    for (; canFallBelow(world, i, j - 1, k) && j > 0; j--) { }
                    if (j > 0)
                    {
                        world.setBlockAndMetadataWithNotify(i, j, k, blockID, meta);
                    }
                }
                else if (!world.isRemote)
                {

                    EntityFallingDirt ent = new EntityFallingDirt(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, blockID, meta, 0);
                    world.spawnEntityInWorld(ent);
                    Random R = new Random(i*j+k);
                    world.playSoundAtEntity(ent, "fallingdirtshort", 1.0F, 0.8F + (R.nextFloat()/2));
                }
            }
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        tryToFall(world, i, j, k);
    }

    public static boolean canFallBelow(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);
        if (l == 0)
        {
            return true;
        }
        if (l == Block.fire.blockID)
        {
            return true;
        }
        Material material = Block.blocksList[l].blockMaterial;
        if (material == Material.water)
        {
            return true;
        }
        return material == Material.lava;
    }
    
    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB par5AxisAlignedBB, ArrayList par6ArrayList)
    {
        if((world.getBlockId(i+1, j, k) == 0 || world.getBlockId(i-1, j, k) == 0 || 
                world.getBlockId(i, j, k+1) == 0 || world.getBlockId(i, j, k-1) == 0) && 
                world.getBlockId(i, j+1, k) == 0)
        {
            par6ArrayList.add(AxisAlignedBB.getBoundingBoxFromPool(i, j, k,i +1,j + 0.5f,k + 1));

            double minX = 0.25;
            double minZ = 0.25;
            double maxX = 0.75;
            double maxZ = 0.75;

            if(world.getBlockId(i+1, j, k) == 0)
                maxX = 0.5;
            if(world.getBlockId(i-1, j, k) == 0)
                minX = 0.5;
            if(world.getBlockId(i, j, k+1) == 0)
                maxZ = 0.5;
            if(world.getBlockId(i, j, k-1) == 0)
                minZ = 0.5;

            par6ArrayList.add(AxisAlignedBB.getBoundingBoxFromPool(i + minX, j + 0.5, k + minZ, i + maxX, j + 1, k + maxZ));

        }
        else
            par6ArrayList.add(AxisAlignedBB.getBoundingBoxFromPool(i, j, k,i + 1,j + 1,k +1));
    }
}