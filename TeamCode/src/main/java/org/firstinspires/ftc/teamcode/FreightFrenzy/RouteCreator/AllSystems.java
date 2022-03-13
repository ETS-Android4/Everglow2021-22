package org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;

public class AllSystems {
    public final LinearOpMode opMode;
    public final ArmSystem armSystem;
    public final DetectionSystem detectionSystem;
    public final DrivingSystem drivingSystem;
    public final DuckSystem duckSystem;
    public final TotemSystem totemSystem;

    public AllSystems(LinearOpMode opMode, ArmSystem armSystem, DetectionSystem detectionSystem, DrivingSystem drivingSystem, DuckSystem duckSystem, TotemSystem totemSystem) {
        this.opMode = opMode;
        this.armSystem = armSystem;
        this.detectionSystem = detectionSystem;
        this.drivingSystem = drivingSystem;
        this.duckSystem = duckSystem;
        this.totemSystem = totemSystem;
    }

    private AllSystems(LinearOpMode opMode) {
        this.opMode = opMode;
        this.armSystem = new ArmSystem(opMode);
        this.detectionSystem = new DetectionSystem(opMode, armSystem);
        this.drivingSystem = new DrivingSystem(opMode);
        this.duckSystem = new DuckSystem(opMode);
        this.totemSystem = new TotemSystem(opMode, false);
    }

    public static AllSystems init(LinearOpMode opMode){
        return new AllSystems(opMode);
    }
}
