package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class AutonomousRoute {
    interface RouteInstruction {
        void execute(AllSystems systems, int mirror);
        String toJavaCode();
    }

    static class DriveStraightInstruction implements RouteInstruction {
        private final double power;
        private final double distance;

        public DriveStraightInstruction(double power, double distance) {
            this.power = power;
            this.distance = distance;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.drivingSystem.driveStraight(distance, power);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.driveStraight(%.1f, %.1f);\n", distance, power);
        }
    }

    static class DriveSidewaysInstruction implements RouteInstruction {
        private final double power;
        private final double distance;

        public DriveSidewaysInstruction(double power, double distance) {
            this.power = power;
            this.distance = distance;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.drivingSystem.driveSideways(distance, power * mirror);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.driveSideways(%.1f, %.1f*mirror);\n", distance, power);
        }
    }

    static class TurnInstruction implements RouteInstruction {
        private final float degrees;
        private final int speedDecrease;

        public TurnInstruction(float degrees, int speedDecrease) {
            this.degrees = degrees;
            this.speedDecrease = speedDecrease;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.drivingSystem.turn(degrees * mirror, speedDecrease);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.turn(%.1f*mirror, %d);\n", degrees, speedDecrease);
        }

    }

    static class DeployDuckInstruction implements RouteInstruction {
        private final long durationMs;

        public DeployDuckInstruction(long durationMs) {
            this.durationMs = durationMs;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.duckSystem.runFor(durationMs);
        }

        @Override
        public String toJavaCode() {
            return String.format("duckSystem.runFor(%d);\n", durationMs);
        }
    }


    static class PlaceFreightInstruction implements RouteInstruction {
        public PlaceFreightInstruction() {
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.armSystem.autonomousPlaceFreight(ArmSystem.Floors.THIRD);
        }

        @Override
        public String toJavaCode() {
            return "armSystem.autonomousPlaceFreight(floor);\n";
        }
    }

    static class ReloadArmInstruction implements RouteInstruction {
        public ReloadArmInstruction() {
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            systems.armSystem.autonomousReload();
        }

        @Override
        public String toJavaCode() {
            return "armSystem.autonomousReload();\n";
        }
    }

    private final List<RouteInstruction> routeInstructions = new ArrayList<>();


    public void execute(AllSystems systems, int mirror) {
        systems.drivingSystem.resetDistance();
        for (RouteInstruction routeInstruction : this.routeInstructions) {
            routeInstruction.execute(systems, mirror);
        }
    }

    public String toJavaCode() {
        StringBuilder sb = new StringBuilder();
        for (RouteInstruction routeInstruction : this.routeInstructions) {
            sb.append(routeInstruction.toJavaCode());
        }
        return sb.toString();
    }

    public void addRouteInstruction(RouteInstruction instruction) {
        routeInstructions.add(instruction);
    }
}
