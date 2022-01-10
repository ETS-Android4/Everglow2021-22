package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

import java.util.ArrayList;
import java.util.List;

public class AutonomousRoute {
    interface RouteInstruction {
        void execute(AllSystems systems);
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
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveStraight(distance, power);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.driveStraight(%f, %f);\n", distance, power);
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
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveSideways(distance, power);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.driveSideways(%f, %f);\n", distance, power);
        }
    }

    static class TurnInstruction implements RouteInstruction{
        private final float degrees;
        private final int speedDecrease;

        public TurnInstruction(float degrees, int speedDecrease) {
            this.degrees = degrees;
            this.speedDecrease = speedDecrease;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.drivingSystem.turn(degrees, speedDecrease);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.turn(%f, %d);\n", degrees, speedDecrease);
        }
    }

    static class DeployDuckInstruction implements RouteInstruction {
        private final long durationMs;

        public DeployDuckInstruction(long durationMs) {
            this.durationMs = durationMs;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.duckSystem.runFor(durationMs);
        }

        @Override
        public String toJavaCode() {
            return String.format("duckSystem.runFor(%d);\n", durationMs);
        }
    }

    static class DriveUntilObstacleInstruction implements RouteInstruction {
        private final double distance;

        public DriveUntilObstacleInstruction(double distance) {
            this.distance = distance;
        }

        @Override
        public void execute(AllSystems systems) {
            systems.drivingSystem.driveUntilObstacle(distance, 1);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.driveUntilObstacle(%f, 1);\n", distance);
        }
    }

    static class CraterPlaceFreightInstruction implements RouteInstruction {
        public CraterPlaceFreightInstruction() {
        }

        @Override
        public void execute(AllSystems systems) {
            Crater crater = new Crater(systems.opMode);
            crater.placeFreight();
        }

        @Override
        public String toJavaCode() {
            return "placeFreight();\n";
        }

    }

    static class CarouselPlaceFreightInstruction implements RouteInstruction {
        public CarouselPlaceFreightInstruction() {
        }

        @Override
        public void execute(AllSystems systems) {
            Carousel carousel = new Carousel(systems.opMode);
            carousel.placeFreight();
        }

        @Override
        public String toJavaCode() {
            return "placeFreight();\n";
        }
    }








        private final List<RouteInstruction> routeInstructions = new ArrayList<>();

    public void execute(AllSystems systems){
        for (RouteInstruction routeInstruction : this.routeInstructions){
            routeInstruction.execute(systems);
        }
    }

    public String toJavaCode(){
        String methodName = "autonomous_generated";
        StringBuilder sb = new StringBuilder();
        for (RouteInstruction routeInstruction : this.routeInstructions){
            sb.append(routeInstruction.toJavaCode());
        }
        return sb.toString();
    }

    public void addRouteInstruction(RouteInstruction routeInstruction){
        routeInstructions.add(routeInstruction);
    }
}
