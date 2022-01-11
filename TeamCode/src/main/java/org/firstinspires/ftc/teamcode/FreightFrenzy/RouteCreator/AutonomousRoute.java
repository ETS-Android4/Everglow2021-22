package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crater;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class AutonomousRoute {
    interface RouteInstruction {
        void execute(AllSystems systems, int mirror);

        String toJavaCode();

        /**
         * If this instruction rotates the robot, then the return value for this method should be true.
         * Used to keep track of mirroring the robot when running the autonoamous on the other side.
         */
        default boolean rotatesRobot(){
            return false;
        }
    }

    // todo: make mirror work


    static class DriveStraightInstruction implements RouteInstruction {
        private final double power;
        private final double distance;
        private final boolean isRobotRotated;

        public DriveStraightInstruction(double power, double distance, boolean isRobotRotated) {
            this.power = power;
            this.distance = distance;
            this.isRobotRotated = isRobotRotated;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            if (isRobotRotated) {
                systems.drivingSystem.driveStraight(distance, power*mirror);
            } else {
                systems.drivingSystem.driveStraight(distance, power);
            }
        }

        @Override
        public String toJavaCode() {
            if (isRobotRotated) {
                return String.format("drivingSystem.driveStraight(%.1f, %.1f*mirror);\n", distance, power);
            } else {
                return String.format("drivingSystem.driveStraight(%.1f, %.1f);\n", distance, power);
            }
        }
    }

    static class DriveSidewaysInstruction implements RouteInstruction {
        private final double power;
        private final double distance;
        private final boolean isRobotRotated;

        public DriveSidewaysInstruction(double power, double distance, boolean isRobotRotated) {
            this.power = power;
            this.distance = distance;
            this.isRobotRotated = isRobotRotated;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            if (isRobotRotated) {
                systems.drivingSystem.driveSideways(distance, power);
            }else {
                systems.drivingSystem.driveSideways(distance, power*mirror);
            }
        }

        @Override
        public String toJavaCode() {
            if (isRobotRotated) {
                return String.format("drivingSystem.driveSideways(%.1f, %.1f);\n", distance, power);
            }else {
                return String.format("drivingSystem.driveSideways(%.1f, %.1f*mirror);\n", distance, power);
            }
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
            systems.drivingSystem.turn(degrees, speedDecrease);
        }

        @Override
        public String toJavaCode() {
            return String.format("drivingSystem.turn(%.1f, %d);\n", degrees, speedDecrease);
        }

        @Override
        public boolean rotatesRobot() {
            // if turning 90 degrees or -90 degrees, this rotates the robot.
            // If turning 180 degrees, then the rotation will keep the rotation the same.
            int degreesRounded = Math.round(degrees);
            return degreesRounded == 90 || degreesRounded == -90;
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

    static class DriveUntilObstacleInstruction implements RouteInstruction {
        private final double distance;
        private final double power;
        private final boolean isRobotRotated;

        public DriveUntilObstacleInstruction(double distance, double power, boolean isRobotRotated) {
            this.distance = distance;
            this.power = power;
            this.isRobotRotated = isRobotRotated;
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            if (isRobotRotated) {
                systems.drivingSystem.moveArmAndDriveUntilObstacle(distance, power*mirror, systems.armSystem);
            }else {
                systems.drivingSystem.moveArmAndDriveUntilObstacle(distance, power, systems.armSystem);
            }
        }

        @Override
        public String toJavaCode() {
            if (isRobotRotated) {
                return String.format("drivingSystem.moveArmAndDriveUntilObstacle(%.1f, %.1f*mirror, armSystem);\n", distance, power);
            }else {
                return String.format("drivingSystem.moveArmAndDriveUntilObstacle(%.1f, %.1f, armSystem);\n", distance, power);
            }
        }
    }

    static class CraterPlaceFreightInstruction implements RouteInstruction {
        public CraterPlaceFreightInstruction() {
        }

        @Override
        public void execute(AllSystems systems, int mirror) {
            Crater crater = new Crater(systems);
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
        public void execute(AllSystems systems, int mirror) {
            Carousel carousel = new Carousel(systems);
            carousel.placeFreight();
        }

        @Override
        public String toJavaCode() {
            return "placeFreight();\n";
        }
    }

    private final List<RouteInstruction> routeInstructions = new ArrayList<>();
    private boolean isRobotRotated = false;


    public boolean isRobotRotated() {
        return isRobotRotated;
    }

    public void execute(AllSystems systems, int mirror) {
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
        if (instruction.rotatesRobot()){
            isRobotRotated = !isRobotRotated;
        }
    }
}
