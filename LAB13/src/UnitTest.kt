import junit.framework.TestCase.assertTrue
import org.junit.Test;

class UnitTest {
    // Continue to the next test immediately, and also rmb to add '@Test'
    @Test
    fun test1(){
        // Good Practice before checking.
        heroes.clear()
        monarchHero = CaoCao()
        heroes.add(CaoCao())
        //Add a role, does not affect the checking anyway
        heroes.add(ZhangFei(MinisterRole()))
        assertTrue(monarchHero.name == "Cao Cao")
    }

    @Test
    fun test2() {
        assertTrue(heroes.size == 2)
    }

    @Test
    fun testCaoDodgeAttack() {
        heroes.clear()
        monarchHero = CaoCao()
        for (i in 0..6) /// assume that there are total 7 heros
            heroes.add(NoneMonarchFactory.createRandomHero())
        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked(){
        if(heroes.isEmpty()) {
            for (i in 0..6) { /// assume that there are total 7 heros
                beingAttacked(NoneMonarchFactory.createRandomHero())
            }
        } else {
            for (hero in heroes){
                beingAttacked(hero)

            }
        }
    }


    fun CaoCaoUnitTest(){

    }

    //    @Test
    fun beingAttacked(hero: Hero) {
        println("Hero's hp:" + hero.hp)
        var spy_role = hero.role
        val spy = object: Hero(spy_role) {
            override val name = hero.name
            override fun beingAttacked() {
                hero.beingAttacked()
                assertTrue(hero.hp >= 0)
            }
        }
        for(i in 0..10)
            spy.beingAttacked()
    }

    object FakeMonarchFactoryFactory: GameObjectFactory {
        override fun getRandomRole(): Role {
            TODO("Unnecessary")
        }

        override fun createRandomHero() : Hero {
            return CaoCao()
        }
    }

    object FakeNonmonarchFactoryFactory: GameObjectFactory {
        var count = 0
        var last: WeiHero? = null
        init {
            monarchHero = CaoCao()
        }
        override fun getRandomRole(): Role =
            MinisterRole()

        override fun createRandomHero(): Hero {
            val hero = when(count++) {
                0->SimaYi(getRandomRole())
                else->XuChu(getRandomRole())
            }
            val cao = monarchHero as CaoCao
            if (last == null)
                cao.helper = hero
            else
                last!!.setNext(hero)
            last = hero
            return hero
        }
    }

    class DummyRole : Role {
        override val roleTitle = "dummy"
        override fun getEnemy() = "dummy"
    }

    @Test
    fun testDiscardCards(){
        val dummy = DummyRole()
        val hero = ZhangFei(dummy)

        val spy = object: WarriorHero(MinisterRole()) {
            override val name = hero.name
            override fun discardCards() {
                super.discardCards()
                assertTrue(hero.numOfCards <= hero.hp)
            }
        }

        spy.drawCards()
        spy.discardCards()
    }
}