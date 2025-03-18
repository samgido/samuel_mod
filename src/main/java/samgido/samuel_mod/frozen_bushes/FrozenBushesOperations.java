package samgido.samuel_mod.frozen_bushes;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import samgido.samuel_mod.ModBlocks;
import samgido.samuel_mod.SamuelMod;

public class FrozenBushesOperations {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> commandDispatcher.register(CommandManager.literal("toggle_berry_freeze").executes(commandContext -> {
            SamuelMod.frozen_berries_toggle = !SamuelMod.frozen_berries_toggle;
            commandContext.getSource().sendFeedback(() -> Text.literal("Toggled berry freeze"), false);
            commandContext.getSource().sendFeedback(() -> Text.literal("Berry freeze: " + SamuelMod.frozen_berries_toggle), false);
            return 1;
        }))));

        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> commandDispatcher.register(CommandManager.literal("get_berry_freeze_state").executes(commandContext -> {
            commandContext.getSource().sendFeedback(() -> Text.literal("Berry freeze: " + SamuelMod.frozen_berries_toggle), false);
            return 1;
        }))));
    }

    public static ActionResult ToggleBerryFreeze(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
        if (!SamuelMod.frozen_berries_toggle) { return ActionResult.PASS; }

        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(blockPos);

        if (player.isSpectator()) return ActionResult.PASS;

        // The following if statement could be refactored, who cares!

        // When the in-hand item check was the same item for freeze and unfreeze, stick, there was an issue of no change happening
        // This method runs many times on a single click, so it was essentially toggling between frozen and unfrozen states
        // Resulting in no change
        // My solution: one item to freeze, another to unfreeze

        ActionResult result = ActionResult.PASS;

        if (state.getBlock() instanceof FrozenSweetBerryBush && player.getMainHandStack().isOf(Items.BRUSH)) {
            int age = state.get(SweetBerryBushBlock.AGE);
            world.setBlockState(blockPos, Blocks.SWEET_BERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, age));

            ItemStack itemStack = player.getMainHandStack();
            EquipmentSlot equipmentSlot = itemStack.equals(player.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;

            itemStack.damage(1, player, equipmentSlot);

            SamuelMod.LOGGER.info("Unfroze bush");
            result = ActionResult.SUCCESS;
        }
        else if (state.getBlock() instanceof SweetBerryBushBlock && player.getMainHandStack().getItem() instanceof DyeItem) {
            int age = state.get(SweetBerryBushBlock.AGE);
            world.setBlockState(blockPos, ModBlocks.FROZEN_SWEET_BERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, age));

            player.getMainHandStack().decrement(1);

            SamuelMod.LOGGER.info("Froze bush");
            result = ActionResult.SUCCESS;
        }

        if (result == ActionResult.SUCCESS) {
            world.addParticle(ParticleTypes.WAX_ON, blockPos.getX(), blockPos.getY() + 1., blockPos.getZ(), 0.0D, 0.0D, 0.0D);
        }

        return result;
    }
}
