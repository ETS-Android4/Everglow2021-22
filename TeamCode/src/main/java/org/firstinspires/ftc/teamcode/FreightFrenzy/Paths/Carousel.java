package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Carousel {
    DrivingSystem    drivingSystem;
    ArmSystem        armSystem;
    DuckSystem       duckSystem;
    TotemSystem      totemSystem;
    DetectionSystem  detectionSystem;
    LinearOpMode     opMode;
    ElapsedTime      timer;
    ArmSystem.Floors floor;

    public Carousel(LinearOpMode opMode) {
        this.opMode     = opMode;
        drivingSystem   = new DrivingSystem(opMode);
        armSystem       = new ArmSystem(opMode);
        duckSystem      = new DuckSystem(opMode);
        totemSystem     = new TotemSystem(opMode);
        detectionSystem = new DetectionSystem(opMode, armSystem);
        timer           = new ElapsedTime();
    }

    public Carousel(AllSystems systems) {
        this.opMode          = systems.opMode;
        this.drivingSystem   = systems.drivingSystem;
        this.armSystem       = systems.armSystem;
        this.duckSystem      = systems.duckSystem;
        this.detectionSystem = systems.detectionSystem;
        timer                = new ElapsedTime();
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight(int mirror) {
        drivingSystem.resetDistance();
        drivingSystem.driveStraight(25, -0.6);
        floor = detectionSystem.findTargetFloor2(mirror);
        this.opMode.telemetry.addData("floor: ", floor);
        this.opMode.telemetry.update();
        drivingSystem.turn(180, 300);

        //avoid totem
        if (mirror == 1) {
            switch (floor) {
                case FIRST:
                    drivingSystem.driveSideways(15, 0.6);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, -0.6);
                    break;
            }
        } else {
            switch (floor) {
                case THIRD:
                    drivingSystem.driveSideways(15, -0.6);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, 0.6);
                    break;
            }
        }
        // move to SH
        drivingSystem.driveStraight(65, 0.6);
        if (mirror == 1) {
            switch (floor) {
                case FIRST:
                    drivingSystem.driveSideways(15, -0.6);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, 0.6);
                    break;
            }
        } else {
            switch (floor) {
                case THIRD:
                    drivingSystem.driveSideways(15, 0.6);
                    break;
                case SECOND:
                    drivingSystem.driveSideways(25, -0.6);
                    break;
            }
        }
        drivingSystem.turn(-90 * mirror, 200);

        // place freight on SH
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(20, 0.6);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.6);
        armSystem.autonomousReload();
    }

    public void placeFreightAndCollectTotem(int mirror) {
        drivingSystem.resetDistance();
        drivingSystem.driveStraight(25, -0.6);
        floor = detectionSystem.findTargetFloor2(mirror);
        this.opMode.telemetry.addData("floor: ", floor);
        this.opMode.telemetry.update();

        switch (floor) {
            case FIRST:
                totemSystem.moveAzimuth(-0.3);
                break;
            case THIRD:
                totemSystem.moveAzimuth(0.3);
        }
        totemSystem.extend(10);
        totemSystem.moveAltitude(0.3);
        totemSystem.extend(-10);
        switch (floor) {
            case FIRST:
                totemSystem.moveAzimuth(0.3);
                break;
            case THIRD:
                totemSystem.moveAzimuth(-0.3);
        }

        drivingSystem.driveStraight(65, -0.6);
        drivingSystem.turn(-90 * mirror, 200);
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(20, 0.6);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(20, -0.6);
        armSystem.autonomousReload();
    }

    /**
     * After placing the freight on the SH, move in front of the Shipping Element.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void dodgeToFront(int mirror) {
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(65, 0.6 * mirror);
                break;
            case SECOND:
                drivingSystem.driveStraight(50, -0.6);
                drivingSystem.driveSideways(65, 0.6 * mirror);
                drivingSystem.driveStraight(50, 0.6);
                break;
            case THIRD:
                drivingSystem.driveStraight(10, -0.6);
                drivingSystem.driveSideways(65, 0.6 * mirror);
                drivingSystem.driveStraight(10, 0.6);
                break;
        }
    }

    /**
     * When going from behind the SH to the front of the right Shipping Element,
     * dodge the Shipping Element.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void dodgeOtherTotem(int mirror) {
        AllSystems systems = new AllSystems(opMode, armSystem, detectionSystem, drivingSystem, duckSystem);
        Crater Crater = new Crater(systems);
        Crater.floor = floor;
        Crater.dodgeToFront(mirror);
    }

    /**
     * After placing the freight on the SH, move to the Carousel.
     *
     * @param mirror 1 is red side, -1 is blue side.
     */
    public void goToCarousel(int mirror) {
        drivingSystem.driveStraight(35, -0.6);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(110, -0.6 * mirror);
    }

    /**
     * Goes to Carousel, and then to Storage Unit.
     */
    public void LZYW(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        drivingSystem.driveStraight(5, 0.5);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        drivingSystem.driveSideways(65, 0.6 * mirror);
        drivingSystem.driveStraight(7, 0.6);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Rams through obstacle.
     */
    public void LBNCO(int mirror) {
        placeFreight(mirror);

        // Go to the right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(mirror);

        // Ram through obstacle
        drivingSystem.turn(180, 200);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(30, -0.6);
        drivingSystem.driveStraight(150, 1);
    }

    /**
     * Goes to Carousel, then to Crater behind SH. Goes through path.
     */
    public void LBNCP(int mirror) {
        placeFreight(mirror);

        // Go to right of the shipping hub and dodge
        drivingSystem.driveSideways(50, -0.6 * mirror);
        drivingSystem.driveStraight(125, 0.6);
        drivingSystem.driveSideways(45, 0.6 * mirror);
        drivingSystem.turn(180, 200);
        dodgeOtherTotem(mirror);

        // Go through path
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(50, 0.6 * mirror);
        drivingSystem.driveStraight(100, 0.6);
    }

    /**
     * Goes to Storage Unit.
     */
    public void LZNW(int mirror) {
        placeFreight(mirror);

        drivingSystem.driveStraight(55, -0.6);
        drivingSystem.driveSideways(30, 0.6 * mirror);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Enters Crater through path.
     */
    public void LFYCP(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        // Go to Crater through path
        drivingSystem.driveSideways(30, 0.7 * mirror);
        drivingSystem.turn(180, 150);
        drivingSystem.driveStraight(100, 0.7);
        drivingSystem.driveSideways(70, 0.7 * mirror);
        drivingSystem.driveStraight(150, 1);
    }

    /**
     * Goes to Carousel, and then to Crater in front of SH. Rams through obstacle.
     */
    public void LFYCO(int mirror) {
        placeFreight(mirror);
        goToCarousel(mirror);
        TimeUtils.sleep(500);
        duckSystem.runFor(3000);

        // Ram through obstacle
        drivingSystem.driveSideways(30, 0.6 * mirror);
        drivingSystem.turn(180, 150);
        armSystem.moveArm(-200);
        drivingSystem.driveStraight(280, 1);
    }
}
