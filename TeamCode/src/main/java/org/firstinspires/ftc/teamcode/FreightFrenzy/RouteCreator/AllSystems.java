package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.CameraSystem3;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem2;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.Side;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class AllSystems {
    public final LinearOpMode opMode;
    public final ArmSystem armSystem;
    public final DrivingSystem drivingSystem;
    public final DuckSystem duckSystem;
    public final TotemSystem2 totemSystem;
    public final CameraSystem3 cameraSystem;
    public final Side side;

    public AllSystems(LinearOpMode opMode, ArmSystem armSystem, DrivingSystem drivingSystem, DuckSystem duckSystem, TotemSystem2 totemSystem, CameraSystem3 cameraSystem, Side side) {
        this.opMode = opMode;
        this.armSystem = armSystem;
        this.drivingSystem = drivingSystem;
        this.duckSystem = duckSystem;
        this.totemSystem = totemSystem;
        this.cameraSystem = cameraSystem;
        this.side = side;

        TimeUtils.opMode = opMode;
    }

    private AllSystems(LinearOpMode opMode, Side side) {
        TimeUtils.opMode = opMode;
        this.opMode = opMode;
        this.armSystem = new ArmSystem(opMode);
        this.drivingSystem = new DrivingSystem(opMode);
        this.duckSystem = new DuckSystem(opMode);
        this.totemSystem = new TotemSystem2(opMode);
        this.cameraSystem = null;
//        this.cameraSystem = new CameraSystem3(opMode, side, this);
        this.side = side;
    }

    public static AllSystems init(LinearOpMode opMode) {
        return init(opMode, Side.RED);
    }

    public static AllSystems init(LinearOpMode opMode, Side side){
        return new AllSystems(opMode, side);
    }

    public void cleanup(){
        cameraSystem.cleanup();
        armSystem.fullStop();
        duckSystem.stop();
        drivingSystem.stop();
    }


}
