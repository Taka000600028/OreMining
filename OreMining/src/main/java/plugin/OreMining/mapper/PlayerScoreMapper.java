package plugin.OreMining.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.OreMining.mapper.data.PlayerScore;

public interface PlayerScoreMapper {

  @Select("SELECT * FROM player_score;")
  List<PlayerScore> selectList();

  @Insert("INSERT INTO player_score(player_name, score, registered_at) values (#{playerName}, #{score}, now())")
  void insert(PlayerScore playerScore);
}
