package de.mennomax.astikorcarts;

import de.mennomax.astikorcarts.entity.AnimalCartEntity;
import de.mennomax.astikorcarts.entity.PlowEntity;
import de.mennomax.astikorcarts.entity.PostilionEntity;
import de.mennomax.astikorcarts.entity.SupplyCartEntity;
import de.mennomax.astikorcarts.inventory.container.PlowContainer;
import de.mennomax.astikorcarts.item.CartItem;
import de.mennomax.astikorcarts.network.NetBuilder;
import de.mennomax.astikorcarts.network.clientbound.UpdateDrawnMessage;
import de.mennomax.astikorcarts.network.serverbound.ActionKeyMessage;
import de.mennomax.astikorcarts.network.serverbound.OpenSupplyCartMessage;
import de.mennomax.astikorcarts.network.serverbound.ToggleSlowMessage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

@Mod(AstikorCarts.ID)
public final class AstikorCarts {
    public static final String ID = "astikorcarts";

    public static final Logger LOGGER = LoggerFactory.getLogger(AstikorCarts.class);

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ID)
                .versioned("1.2.3")
                .optional();
    }

    public static final SimpleChannel CHANNEL = new NetBuilder(new ResourceLocation(ID, "main"))
            .version(1).optionalServer().requiredClient()
            .serverbound(ActionKeyMessage::new).consumer(() -> ActionKeyMessage::handle)
            .serverbound(ToggleSlowMessage::new).consumer(() -> ToggleSlowMessage::handle)
            .clientbound(UpdateDrawnMessage::new).consumer(() -> new UpdateDrawnMessage.Handler())
            .serverbound(OpenSupplyCartMessage::new).consumer(() -> OpenSupplyCartMessage::handle)
            .build();


    public class ACStats {

        public static final DeferredRegister<ResourceLocation> AC_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, ID);
        public static final Supplier<ResourceLocation> CART_ONE_CM = AC_STATS.register("cart_one_cm", () -> makeStat("cart_one_cm"));
        private static ResourceLocation makeStat(String key) {
            return new ResourceLocation(ID, key);
        }
        public static void initStats() {
            Stats.CUSTOM.get(CART_ONE_CM.get(), StatFormatter.DISTANCE);
        }
    }
    public static final class Items {
        public static final DeferredRegister<Item> R = DeferredRegister.create(BuiltInRegistries.ITEM, ID);



        public static final Supplier<Item> WHEEL, SUPPLY_CART, PLOW, ANIMAL_CART;

        static {
            WHEEL = R.register("wheel", () -> new Item(new Item.Properties()));
            final Supplier<Item> cart = () -> new CartItem(new Item.Properties().stacksTo(1));
            SUPPLY_CART = R.register("supply_cart", cart);
            PLOW = R.register("plow", cart);
            ANIMAL_CART = R.register("animal_cart", cart);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept((ItemLike) Items.ANIMAL_CART);
            event.accept((ItemLike) Items.SUPPLY_CART);
            event.accept((ItemLike) Items.PLOW);
            event.accept((ItemLike) Items.WHEEL);
        }
    }

    public static final class EntityTypes {
        private EntityTypes() {
        }
        public static final DeferredRegister<EntityType<?>> R = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ID);

        public static final Supplier<EntityType<SupplyCartEntity>> SUPPLY_CART;
        public static final Supplier<EntityType<PlowEntity>> PLOW;
        public static final Supplier<EntityType<AnimalCartEntity>> ANIMAL_CART;
        public static final Supplier<EntityType<PostilionEntity>> POSTILION;

        static {
            SUPPLY_CART = R.register("supply_cart", () -> EntityType.Builder.of(SupplyCartEntity::new, MobCategory.MISC)
                    .sized(1.5F, 1.4F)
                    .build(ID + ":supply_cart"));
            PLOW = R.register("plow", () -> EntityType.Builder.of(PlowEntity::new, MobCategory.MISC)
                    .sized(1.3F, 1.4F)
                    .build(ID + ":plow"));
            ANIMAL_CART = R.register("animal_cart", () -> EntityType.Builder.of(AnimalCartEntity::new, MobCategory.MISC)
                    .sized(1.3F, 1.4F)
                    .build(ID + ":animal_cart"));
            POSTILION = R.register("postilion", () -> EntityType.Builder.of(PostilionEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .noSummon()
                    .noSave()
                    .build(ID + ":postilion"));
        }
    }

    public static final class SoundEvents {

        private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ID);

        public static final Supplier<SoundEvent> CART_ATTACHED = registerSoundEvent("entity.cart.attach");
        public static final Supplier<SoundEvent> CART_DETACHED = registerSoundEvent("entity.cart.detach");
        public static final Supplier<SoundEvent> CART_PLACED = registerSoundEvent("entity.cart.place");

        private static Supplier<SoundEvent> registerSoundEvent(String name) {
            ResourceLocation id = new ResourceLocation(ID, name);
            return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
        }
    }

    public static final class ContainerTypes {
        private ContainerTypes() {
        }

        private static final DeferredRegister<MenuType<?>> R = DeferredRegister.create(BuiltInRegistries.MENU, ID);

        public static final Supplier<MenuType<PlowContainer>> PLOW_CART = R.register("plow", () -> IMenuTypeExtension.create(PlowContainer::new));


    }
    public AstikorCarts(IEventBus bus) {
        bus.addListener(EventPriority.NORMAL, this::setup);
        Items.R.register(bus);
        EntityTypes.R.register(bus);
        SoundEvents.SOUND_EVENTS.register(bus);
        ContainerTypes.R.register(bus);
        ACStats.AC_STATS.register(bus);

        bus.addListener(this::addCreative);
    }
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ACStats::initStats);
    }
}